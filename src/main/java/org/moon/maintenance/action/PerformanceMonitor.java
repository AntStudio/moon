package org.moon.maintenance.action;

import org.logicalcobwebs.proxool.ProxoolDataSource;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.logicalcobwebs.proxool.admin.SnapshotIF;
import org.moon.maintenance.component.PerformanceMonitorDataHolder;
import org.moon.message.WebResponse;
import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rbac.domain.annotation.PermissionMapping;
import org.moon.rbac.helper.Permissions;
import org.moon.rest.annotation.Get;
import org.moon.utils.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * 性能监控
 * @author:Gavin
 * @date 2015/2/6 0006
 */
@Controller
@RequestMapping("performance")
@PermissionMapping(code = Permissions.PERFORMANCE_MONITOR, name = Permissions.PERFORMANCE_MONITOR_DESCRIPTION)
public class PerformanceMonitor {

    @Resource
    private PerformanceMonitorDataHolder performanceMonitorDataHolder;

    @Resource
    private ProxoolDataSource dataSource;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Get("")
    @MenuMapping(url = "/performance" , name = "性能监控" ,code = "platform_performance" , parentCode = "platform")
    public ModelAndView showPerformancePage(){
        return new ModelAndView("pages/maintenance/performanceMonitor");
    }

    /**
     * 获取监控数据
     * @return
     */
    @Get("/data")
    @ResponseBody
    public WebResponse getPerformanceData() throws ProxoolException {
        return WebResponse.build().setResult(
                Maps.mapIt("interfaceInfo",performanceMonitorDataHolder.getTimeStatisticsData(),
                        "dbStatus",getDBStatus()
                ));
    }

    /**
     * 获取数据库连接池信息
     * @return
     * @throws ProxoolException
     */
    private Map<String,Object> getDBStatus() throws ProxoolException {
        SnapshotIF snapshotIF = ProxoolFacade.getSnapshot(dataSource.getAlias());
        snapshotIF.getActiveConnectionCount();
        return Maps.mapIt("sumCount",snapshotIF.getMaximumConnectionCount(),
                "availableCount",snapshotIF.getAvailableConnectionCount(),
                "activeCount",snapshotIF.getActiveConnectionCount(),
                "date",sdf.format(snapshotIF.getDateStarted()),//连接池创建时间
                "servedCount",snapshotIF.getServedCount(),//已服务的连接数
                "refusedCount",snapshotIF.getRefusedCount());//已拒绝的连接数
    }
}
