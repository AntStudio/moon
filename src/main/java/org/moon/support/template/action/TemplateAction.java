package org.moon.support.template.action;

import org.moon.message.WebResponse;
import org.moon.rbac.domain.User;
import org.moon.rbac.domain.annotation.LoginRequired;
import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rbac.domain.annotation.WebUser;
import org.moon.rest.annotation.Delete;
import org.moon.rest.annotation.Get;
import org.moon.rest.annotation.Post;
import org.moon.rest.annotation.Put;
import org.moon.support.template.domain.Template;
import org.moon.support.template.repository.TemplateRepository;
import org.moon.support.template.service.TemplateService;
import org.moon.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 模板管理
 * @author:Gavin
 * @date 2015/6/16 0016
 */
@RestController
@RequestMapping("/templates")
@LoginRequired
public class TemplateAction {

    @Resource
    private TemplateService templateService;

    @Get
    public WebResponse getTemplates(HttpServletRequest request,@WebUser User user){
        return WebResponse.success(templateService.listForPage(TemplateRepository.class,"list", ParamUtils.getParamsMapForPager(request)));
    }

    @Post
    public WebResponse addTemplate(@RequestBody Template template, @WebUser User user){
        template.setUser(user);
        templateService.save(template);
        return WebResponse.success(template);
    }

    @Put("/{id}")
    public WebResponse updateTemplate(@PathVariable Long id,@RequestBody Template template, @WebUser User user){
        template.setId(id);
        template.setUser(user);
        templateService.update(template);
        return WebResponse.success(template);
    }

    @Delete("/{id}")
    public WebResponse deleteTemplate(@PathVariable Long id, @WebUser User user){
        templateService.delete(id);
        return WebResponse.success();
    }

    @Get("/{id}")
    public WebResponse getTemplate(@PathVariable Long id,@WebUser User user){
        return WebResponse.success(templateService.get(id));
    }
}
