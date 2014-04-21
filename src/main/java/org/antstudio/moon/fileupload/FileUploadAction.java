package org.antstudio.moon.fileupload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antstudio.moon.rest.annotation.Post;
import org.antstudio.support.session.SessionContext;
import org.antstudio.utils.FileUtils;
import org.antstudio.utils.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadAction {

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
    public @ResponseBody Map uploadFile(@RequestParam("files") MultipartFile[]  files){
    	List<Map<String,String>> success = new ArrayList<Map<String,String>>();
    	List<Map<String,String>> failure = new ArrayList<Map<String,String>>();
    	File out;
    	for(MultipartFile file:files){
    		File f = new File(SessionContext.getWebAppPath()+"upload/"+file.getOriginalFilename());
    		if(f.exists()){
    			f = new File(SessionContext.getWebAppPath()+"upload/"+file.getOriginalFilename());
    		}
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
    
}
