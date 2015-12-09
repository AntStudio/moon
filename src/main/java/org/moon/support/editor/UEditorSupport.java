package org.moon.support.editor;

import org.moon.rest.annotation.Get;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * UEditor后台配置接口
 * @author:Gavin
 * @date 2015/6/12 0012
 */
@RestController
@RequestMapping("/ueditor")
public class UEditorSupport {

    @Get("/config")
    @ResponseBody
    public String getConfig(){
        return "{}";
    }

}
