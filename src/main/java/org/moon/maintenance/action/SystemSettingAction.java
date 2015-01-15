package org.moon.maintenance.action;

import org.moon.core.cache.memcached.MemCachedManager;
import org.moon.core.spring.config.annotation.Config;
import org.moon.maintenance.service.SystemSettingService;
import org.moon.message.WebResponse;
import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rest.annotation.Get;
import org.moon.rest.annotation.Post;
import org.moon.utils.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统设置
 * @author:Gavin
 * @date 2014/11/18 0018
 */
@Controller
@RequestMapping("/setting")
public class SystemSettingAction {
    @Config(value = "memcached.open",defaultVal = "true")
    private boolean openMemCached;

    @Config(value = "memcached.host",defaultVal = "127.0.0.1")
    private String hostName;

    @Config(value = "memcached.port",defaultVal = "11211")
    private int portNum;

    @Resource
    private SystemSettingService systemSettingService;

    @Resource
    private MemCachedManager memCachedManager;

    @Get("")
    @MenuMapping(name = "系统设置",code = "platform_9",parentCode = "platform",url = "/setting")
    public ModelAndView showPage(){
        return new ModelAndView("/pages/maintenance/SystemSetting");
    }

    @Post("/update")
    @ResponseBody
    public WebResponse update(HttpServletRequest request,@RequestParam("memcached.open")String open,
                              @RequestParam("memcached.host")String host,@RequestParam("memcached.port")String port){

        if("true".equals(open)){//打开缓存
            memCachedManager.openCache(host,Integer.parseInt(port));
        }else{
            memCachedManager.closeCache();
        }
        Map<String,String> settings = Maps.mapIt("memcached.open",open,"memcached.host",host,"memcached.port",port);
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
        Map<Object,Object> settingMap = new HashMap<Object,Object>();
        settingMap.put("memcached.open",openMemCached);
        settingMap.put("memcached.host",hostName);
        settingMap.put("memcached.port",portNum);
        settingMap.putAll(systemSettingService.getSettingMap());
        return WebResponse.build().setResult(settingMap);
    }

}
