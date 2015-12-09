package org.moon.maintenance.action;

import org.moon.core.spring.config.annotation.Config;
import org.moon.dictionary.service.DictionaryService;
import org.moon.maintenance.service.SystemSettingService;
import org.moon.message.WebResponse;
import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rbac.domain.annotation.PermissionMapping;
import org.moon.rbac.helper.Permissions;
import org.moon.rest.annotation.Get;
import org.moon.rest.annotation.Post;
import org.moon.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 系统设置
 * @author GavinCook
 * @date 2014/11/18 0018
 */
@Controller
@RequestMapping("/setting")
@PermissionMapping(code = Permissions.SYSTEM_SETTING, name = Permissions.SYSTEM_SETTING_DESCRIPTION)
public class SystemSettingAction {

    @Resource
    private SystemSettingService systemSettingService;

    @Resource
    private DictionaryService dictionaryService;

    @Config("dic.userType")
    private String userTypeDicCode;

    @Get("")
    @MenuMapping(name = "系统设置",code = "platform_9",parentCode = "platform",url = "/setting")
    public ModelAndView showPage(){
        return new ModelAndView("/pages/maintenance/SystemSetting",
                "userTypes", dictionaryService.listChildrenByCode(userTypeDicCode));
    }

    @Post("/update")
    @ResponseBody
    public WebResponse update(HttpServletRequest request){
        Map<String,Object> settings = ParamUtils.getParamMapFromRequest(request);
        systemSettingService.updateSetting(settings);
        return WebResponse.build();
    }

    /**
     * 获取系统配置信息，如果数据表未保存配置信息则从配置文件读取
     * @param request
     * @return
     */
    @Get("/list")
    @ResponseBody
    public WebResponse get(HttpServletRequest request){
        String a = systemSettingService.getSetting("email.statistics","");
        return WebResponse.success(systemSettingService.getSettingMap());
    }

}
