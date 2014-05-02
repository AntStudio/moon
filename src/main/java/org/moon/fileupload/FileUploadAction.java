package org.moon.fileupload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.moon.rest.annotation.Post;
import org.moon.support.session.SessionContext;
import org.moon.support.spring.config.annotation.Config;
import org.moon.utils.FileUtils;
import org.moon.utils.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadAction {

	@Config("upload.baseDir")
	private String baseDir;
	
	private File baseDirFile;
	public FileUploadAction(){
		
	}
	
	/**
	 * 文件上传,主要用于配合前端web uploader.使用此控制器时需要将web uploader.
	 * @param file
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
	 * @throws IOException 
	 */
    @Post("/file/upload")
    public @ResponseBody Map uploadFile(@RequestParam("files") MultipartFile[]  files) throws IOException{
    	List<Map<String,String>> success = new ArrayList<Map<String,String>>();
    	List<Map<String,String>> failure = new ArrayList<Map<String,String>>();
    	File out;
    	
    	if(baseDirFile==null||!baseDirFile.exists()){
    		createBaseDirIfNecessary();
    	}
    	
    	for(MultipartFile file:files){
    		File f = new File(baseDirFile,file.getOriginalFilename());
    		try{
    			out = FileUtils.getFileNotExists(f);
    			FileUtils.save(file.getInputStream(),out);
    			success.add(Maps.mapIt("originName",file.getOriginalFilename(),
    								   "fileName",out.getName(),
    								   "filePath",out.getAbsolutePath().replace(SessionContext.getWebAppPath(), "")));
    		}catch (Exception e) {
    			failure.add(Maps.mapIt("originName",file.getOriginalFilename(),"errorMsg",e.getMessage()));
			}
    	}
        return Maps.mapIt("success",success,"failure",failure);
    }
    
    private void createBaseDirIfNecessary() throws IOException{
    	if(baseDir==null){//默认上传路径webappPath/upload/
    		baseDir = SessionContext.getWebAppPath()+"upload";
    	}else if(baseDir.startsWith("${webappPath}")){
    		baseDir = baseDir.replace("${webappPath}", SessionContext.getWebAppPath());
    	}
		this.baseDirFile = FileUtils.createIfNotExists(baseDir,true);
    }
}
