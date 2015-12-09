package org.moon.maintenance.component;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import org.moon.utils.Maps;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监控数据管理
 * @author:Gavin
 * @date 2015/2/6 0006
 */
@Component
public class PerformanceMonitorDataHolder {

    private final ConcurrentHashMap<String,Timer> timeStatistics = new ConcurrentHashMap<String, Timer>(64);

    private final MetricRegistry metrics = new MetricRegistry();

    private final int num = 1000000;//用于纳秒转换为秒
    /**
     * 获取每个uri对应的timer，如果不存在则创建
     * @param key
     * @return
     */
    public Timer getTimer(String key){
        if(timeStatistics.containsKey(key)){
            return timeStatistics.get(key);
        }else{
            Timer timer = metrics.timer(key);
            timeStatistics.put(key,timer);
            return timer;
        }
    }

    /**
     * 获取监控数据
     * @return
     */
    public List<Map<String,Object>> getTimeStatisticsData(){
        List<Map<String,Object>> statisticsData = new ArrayList<Map<String, Object>>(62);
        Snapshot snapshot;
        Timer timer;
        for(Map.Entry<String,Timer> entry : timeStatistics.entrySet()){
            timer = entry.getValue();
            snapshot = timer.getSnapshot();
            statisticsData.add(Maps.mapIt(
                    "interface",entry.getKey(),
                    "max",snapshot.getMax()/num,
                    "mean",snapshot.getMean()/num,
                    "count",timer.getCount(),
                    "95th",snapshot.get95thPercentile()/num,
                    "75th",snapshot.get75thPercentile()/num,
                    "99th",snapshot.get99thPercentile()/num));
        }
        return statisticsData;
    }

}
