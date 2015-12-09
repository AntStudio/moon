package org.moon.view;

import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rest.annotation.Get;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

/**
 * 页面控制类
 * @author:Gavin
 * @date 2015/6/16 0016
 */
@Controller("~pageAction")
public class PageAction {

    /**
     * 模板管理页面
     * @return
     */
    @Get("/template.html")
    @MenuMapping(name = "模板管理", code = "template", parentCode = "platform", url = "/template.html")
    public ModelAndView showTemplatePage(){
        return new ModelAndView("pages/template/templateManagement");
    }


}
