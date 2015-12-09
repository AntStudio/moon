package org.moon.fileupload;

import org.moon.core.spring.ApplicationContextHelper;
import org.moon.core.spring.config.annotation.Config;
import org.moon.rest.annotation.Get;
import org.moon.rest.annotation.Post;
import org.moon.utils.FileUtils;
import org.moon.utils.Images;
import org.moon.utils.Maps;
import org.moon.utils.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class FileUploadAction implements InitializingBean,ApplicationContextAware{

	@Config("upload.baseDir")
	private String baseDir;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private ApplicationContext applicationContext ;

    private int threshold = 1 * 1024 * 1024;//压缩阀值，1m
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
	 * @throws IOException 
	 */
    @Post("/file/upload")
    @ResponseBody
    public Map uploadFile(@RequestParam("files") MultipartFile[]  files,
                          @RequestParam(value = "height",required = false,defaultValue = "300")int height,
                          @RequestParam(value = "width",required = false,defaultValue = "300")int width){
    	List<Map<String,String>> success = new ArrayList<Map<String,String>>();
    	List<Map<String,String>> failure = new ArrayList<Map<String,String>>();
    	File out;
        int baseDirLength = baseDir.length()-1;
    	for(MultipartFile file:files){

    		File f = new File(FileUtils.getTimestampPath(baseDir),System.currentTimeMillis()+"."+getExtName(file.getOriginalFilename()));
    		try{
                out = FileUtils.getFileNotExists(f);
                FileUtils.createIfNotExists(out.getAbsolutePath(), false);

                File originImageFile = FileUtils.createIfNotExists(out.getParent()+File.separator+"origin_"+out.getName(), false);
                byte[] data = new byte[file.getInputStream().available()];

                InputStream in = file.getInputStream();
                in.read(data);
                in.close();

                FileUtils.save(data,originImageFile);//保存原图,原图直接保存，避免imageIO读取后变红

                BufferedImage originImage = ImageIO.read(new ByteArrayInputStream(data));

                float quality;
                if(file.getSize()>threshold){
                    quality = 0.8f;
                    originImage = Images.compressImage(originImage, quality);
                }

                BufferedImage imageScaled = Images.scaleImage(originImage,width,height,true,false);
                ImageIO.write(imageScaled,"jpg",out);

                success.add(Maps.mapIt("originName",file.getOriginalFilename(),
    								   "fileName",out.getName(),
    								   "filePath",out.getAbsolutePath().substring(baseDirLength)));
            }catch (Exception e) {
    			failure.add(Maps.mapIt("originName",file.getOriginalFilename(),"errorMsg",e.getMessage()));
			}
    	}
        return Maps.mapIt("success",success,"failure",failure);
    }

    @Post("/file/upload/images")
    @ResponseBody
    public Map uploadImage(@RequestParam("files") MultipartFile[]  files ,
                           @RequestParam(value = "avatar",defaultValue = "false") Boolean isAvatar){
        List<Map<String,String>> success = new ArrayList<Map<String,String>>();
        List<Map<String,String>> failure = new ArrayList<Map<String,String>>();
        File out;
        int baseDirLength = baseDir.length()-1;

        for(MultipartFile file:files){

            File f = new File(FileUtils.getTimestampPath(baseDir),System.currentTimeMillis()+"."+getExtName(file.getOriginalFilename()));
            try{
                out = FileUtils.getFileNotExists(f);
                FileUtils.createIfNotExists(out.getAbsolutePath(), false);

                File originImageFile = FileUtils.createIfNotExists(out.getParent()+File.separator+"origin_"+out.getName(), false);
                byte[] data = new byte[file.getInputStream().available()];

                InputStream in = file.getInputStream();
                in.read(data);
                in.close();

                FileUtils.save(data,originImageFile);//保存原图,原图直接保存，避免imageIO读取后变红

                BufferedImage originImage = ImageIO.read(new ByteArrayInputStream(data));

                float quality;
                if(file.getSize()>threshold){
                    quality = 0.8f;
                    originImage = Images.compressImage(originImage, quality);
                }


                if(isAvatar){//如果是头像需要保存一个200*200的尺寸，并且将200*200设为默认
                    originImage = Images.toSquare(originImage);//正方形
                    BufferedImage image200 = Images.scaleImage(originImage,200,200,true,false);
                    ImageIO.write(image200,"jpg",out);
                    BufferedImage image100 = Images.scaleImage(originImage,100,100,true,false);
                    ImageIO.write(image100,"jpg",new File(out.getParentFile(),"100_"+out.getName()));
                }else{//否则保存100*100的缩略图，并设为默认
                    BufferedImage image100 = Images.scaleImage(originImage,100,100,true,false);
                    ImageIO.write(image100,"jpg",out);
                }

                success.add(Maps.mapIt("originName",file.getOriginalFilename(),
                        "fileName",out.getName(),
                        "filePath",out.getAbsolutePath().substring(baseDirLength).replaceAll("\\\\","/")));
            }catch (Exception e) {
                failure.add(Maps.mapIt("originName",file.getOriginalFilename(),"errorMsg",e.getMessage()));
            }
        }

        return Maps.mapIt("success",success,"failure",failure);
    }

    /**
     * Base64图片上传，返回格式见#{@link #uploadFile(org.springframework.web.multipart.MultipartFile[], int, int)}
     * @param content
     * @return
     */
    @Post("/file/base64/upload")
    @ResponseBody
    public Map uploadBase64Image(@RequestParam("content")String[] content,
                                 @RequestParam(value = "avatar",defaultValue = "false") Boolean isAvatar){
        if (Objects.nonNull(content) && content.length > 0){
            int baseDirLength = baseDir.length()-1;
            List<Map<String,String>> success = new ArrayList<Map<String,String>>();
            List<Map<String,String>> failure = new ArrayList<Map<String,String>>();
            BASE64Decoder decoder = new BASE64Decoder();
            File out;
            for(String imgStr : content) {
                try {
                    //Base64解码
                    byte[] data = decoder.decodeBuffer(imgStr);
                    for (int i = 0; i < data.length; ++i) {
                        if (data[i] < 0) {//调整异常数据
                            data[i] += 256;
                        }
                    }
                    File f = new File(FileUtils.getTimestampPath(baseDir),System.currentTimeMillis()+".jpg");
                    out = FileUtils.getFileNotExists(f);
                    FileUtils.createIfNotExists(out.getAbsolutePath(), false);

                    File originImageFile = FileUtils.createIfNotExists(out.getParent()+File.separator+"origin_"+out.getName(), false);
                    FileUtils.save(data,originImageFile);//保存原图,原图直接保存，避免imageIO读取后变红

                    BufferedImage originImage = ImageIO.read(new ByteArrayInputStream(data));
                    float quality = 1.0f;
                    if(content.length>threshold){//大于1m才压缩
                        quality = 0.8f;
                        originImage = Images.compressImage(originImage, quality);//压缩
                    }

                    if(isAvatar){//如果是头像需要保存一个200*200的尺寸，并且将200*200设为默认
                        originImage = Images.toSquare(originImage);//正方形
                        BufferedImage image200 = Images.scaleImage(originImage,200,200,true,false);
                        ImageIO.write(image200,"jpg",out);
                        BufferedImage image100 = Images.scaleImage(originImage,100,100,true,false);
                        ImageIO.write(image100,"jpg",new File(out.getParentFile(),"100_"+out.getName()));
                    }else{
                        BufferedImage image100 = Images.scaleImage(originImage,100,100,true,false);
                        ImageIO.write(image100,"jpg",out);
                    }

                    success.add(Maps.mapIt(
                            "fileName", out.getName(),
                            "filePath", out.getAbsolutePath().substring(baseDirLength).replaceAll("\\\\","/")));
                } catch (Exception e) {
                    failure.add(Maps.mapIt(
                            "errorMsg",e.getMessage()));
                    e.printStackTrace();
                }
            }
            return Maps.mapIt("success",success,"failure",failure);
        }
        return Maps.mapIt("success",new String[0],"failure",new String[0]);
    }
    /**
     * 暂时只支持图片获取,{url:.*}是为了把路径中的后缀名也作为路径一起传过来
     * @param request
     * @param response
     * @throws IOException
     */
    @Get("/file/get/**")
    public void getPic(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String url = uri.substring(uri.indexOf("/file/get/") + 10);//10 = "/file/get/".length()
        int jSessionIdPosition = url.indexOf(";jsessionid");
        if(jSessionIdPosition!=-1){
            url = url.substring(0,jSessionIdPosition);
        }
        File file = new File(baseDir,url);
        response.setHeader("Content-type","image/"+FileUtils.getExtname(file));
        FileUtils.write(new FileInputStream(file),response.getOutputStream());
    }

    private void createBaseDirIfNecessary(String webPath) throws IOException{
    	if(baseDir==null){//默认上传路径webappPath/upload/
    		baseDir = webPath+File.separator+"upload";
    	}else if(baseDir.startsWith("${webappPath}")){
    		baseDir = baseDir.replace("${webappPath}",webPath ).replaceAll("[/\\\\]+",File.separator.equals("\\")?File.separator+File.separator:File.separator);
    	}
        if(!baseDir.endsWith(File.separator)){
            baseDir = baseDir + File.separator;
        }
		FileUtils.createIfNotExists(baseDir, true);
    }

    /**
     * 设置applicationcontext,然后在{@link FileUploadAction#afterPropertiesSet()}中调用,因为setApplicationContext会早于
     * BeanPostProcessor接口执行,会导致@Config还没来得及处理.
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       this.applicationContext = applicationContext;
    }

    /**
     * 创建必要的目录,以供上传
     * @throws BeansException
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            createBaseDirIfNecessary(ApplicationContextHelper.getWebAppPath(applicationContext));
        } catch (IOException e) {
            log.error("The File upload base dir create fail.", e.getCause());
        }
    }

    /**
     * 根据文件名获取后缀名
     * @param name
     * @return
     */
    private String getExtName(String name){
        int position = name.lastIndexOf(".");
        if(position != -1){
            return name.substring(position+1);
        }
        return "";
    }

}
