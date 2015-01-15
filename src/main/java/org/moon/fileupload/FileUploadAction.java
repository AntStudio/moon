package org.moon.fileupload;

import org.moon.core.spring.ApplicationContextHelper;
import org.moon.rest.annotation.Get;
import org.moon.rest.annotation.Post;
import org.moon.core.session.SessionContext;
import org.moon.core.spring.config.annotation.Config;
import org.moon.utils.FileUtils;
import org.moon.utils.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class FileUploadAction implements InitializingBean,ApplicationContextAware{

	@Config("upload.baseDir")
	private String baseDir;
	
	private File baseDirFile;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private ApplicationContext applicationContext ;

	/**
	 * 文件上传,主要用于配合前端web uploader.使用此控制器时需要将web uploader.
	 * @param files
	 * @return 返回上传的结果，包括成功和失败的文件上传。如
	 * <pre>
	 * {"success":
	 *           [
	 *           	{	
	 *           		"originName":"test.png",
	 *            		"fileName":"test(1).png",
	 *             		"filePath":"/upload/test(1).png"
	 *              }
	 *           ],
	 * "failure":[
	 *        		{
	 *        			"originName":"failure.png"
	 *        		}
	 *        	 ]
	 *}
	 * </pre>
	 * @throws java.io.IOException
	 */
    @Post("/file/upload")
    public @ResponseBody Map uploadFile(@RequestParam("files") MultipartFile[]  files){
    	List<Map<String,String>> success = new ArrayList<Map<String,String>>();
    	List<Map<String,String>> failure = new ArrayList<Map<String,String>>();
    	File out;

    	for(MultipartFile file:files){
    		File f = new File(baseDirFile,file.getOriginalFilename());
    		try{
    			out = FileUtils.getFileNotExists(f);
    			FileUtils.save(file.getInputStream(),out);
    			success.add(Maps.mapIt("originName",file.getOriginalFilename(),
    								   "fileName",out.getName(),
    								   "filePath",file.getOriginalFilename()));
    		}catch (Exception e) {
    			failure.add(Maps.mapIt("originName",file.getOriginalFilename(),"errorMsg",e.getMessage()));
			}
    	}
        return Maps.mapIt("success",success,"failure",failure);
    }

    /**
     * 暂时只支持图片获取,{url:.*}是为了把路径中的后缀名也作为路径一起传过来
     * @param request
     * @param response
     * @param url
     * @throws java.io.IOException
     */
    @Get("/file/get/{url:.*}")
    public void getPic(HttpServletRequest request,HttpServletResponse response,@PathVariable("url")String url) throws IOException {
        File file = new File(baseDir,url);
        response.setHeader("Content-type","image/"+FileUtils.getExtname(file));
        FileUtils.write(new FileInputStream(file),response.getOutputStream());
    }
    private void createBaseDirIfNecessary(String webPath) throws IOException{
    	if(baseDir==null){//默认上传路径webappPath/upload/
    		baseDir = webPath+File.pathSeparator+"upload";
    	}else if(baseDir.startsWith("${webappPath}")){
    		baseDir = baseDir.replace("${webappPath}",webPath );
    	}

		this.baseDirFile = FileUtils.createIfNotExists(baseDir,true);
    }


    /**
     * 设置applicationcontext,然后在{@link org.moon.fileupload.FileUploadAction#afterPropertiesSet()}中调用,因为setApplicationContext会早于
     * BeanPostProcessor接口执行,会导致@Config还没来得及处理.
     * @param applicationContext
     * @throws org.springframework.beans.BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       this.applicationContext = applicationContext;
    }

    /**
     * 创建必要的目录,以供上传
     * @throws org.springframework.beans.BeansException
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if(baseDirFile==null||!baseDirFile.exists()){
            try {
                createBaseDirIfNecessary(ApplicationContextHelper.getWebAppPath(applicationContext));
            } catch (IOException e) {
                log.error("The File upload base dir create fail.",e.getCause());
            }
        }
    }
}
