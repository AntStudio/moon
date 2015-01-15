package org.moon.core.cache.memcached;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;
import org.moon.core.cache.guava.GuavaCacheManager;
import org.moon.core.spring.config.annotation.Config;
import org.moon.exception.ApplicationRunTimeException;
import org.moon.maintenance.service.SystemSettingService;
import org.moon.utils.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * MemCached 缓存管理器，包括缓存配置、获取、存放等操作
 * @author:Gavin
 * @date 2014/11/18 0018
 */
@Component
public class MemCachedManager implements ApplicationContextAware{

    @Config(value = "memcached.open",defaultVal = "true")
    private boolean openMemCached;

    @Config(value = "memcached.host",defaultVal = "127.0.0.1")
    private String hostName;

    @Config(value = "memcached.port",defaultVal = "11211")
    private int portNum;

    private MemcachedClient memcachedClient;

    @Resource
    private SystemSettingService systemSettingService;

    @Resource
    private GuavaCacheManager guavaCacheManager;

    private long period = 5 * 60 * 1000;//每隔5分钟进行重连（ms）

    private Timer timer = new Timer(true);

    //是否可用
    private boolean available = true;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            try{
                checkConnect();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    //是否正在连接
    private boolean connecting = false;

    public void openCache(String host,int port){
        try {
            if(!hostName.equals(host)||portNum != port||!openMemCached){
                closeCache();
                memcachedClient = new MemcachedClient(new InetSocketAddress(hostName, portNum));
                hostName = host;
                portNum = port;
                openMemCached = true;
                stopTimer();
            }
        } catch (IOException e) {
            throw new ApplicationRunTimeException(e.getCause());
        }
    }

    public void closeCache(){
       if(Objects.nonNull(memcachedClient)){
           memcachedClient.shutdown();
       }
       this.openMemCached = false;
    }

    /**
     * 获取缓存客户端
     * @return
     */
    public MemcachedClient getMemcachedClient(){
        return memcachedClient;
    }

    public void set(String key,int exp,Object o){
        if(!available){//不可用时
            log.debug("put data in guava for key : {}",key);
            guavaCacheManager.put(key,o);
        }else {
            try {
                log.debug("trying put data in memcached for key : {}",key);
                memcachedClient.set(key, exp, o);
                log.debug("failed put data in memcached for key : {}",key);
            } catch (Exception e) {
                log.debug("put data in guava for key : {}",key);
                startTimer();
                guavaCacheManager.put(key, o);
            }
        }
    }

    /**
     * 获取key对应的缓存值
     * @param key
     * @return
     */
    public Object get(String key){
        if(!available){//不可用时
            log.debug("Get data from guava for key : {}",key);
            return guavaCacheManager.get(key);
        }else {
            try {
                log.debug("Get data from memcached for key : {}",key);
                return memcachedClient.get(key);
            } catch (Exception e) {
                log.debug("failed get data from memcached for key : {}",key);
                log.debug("Get data from guava for key : {}",key);
                startTimer();
                return guavaCacheManager.get(key);
            }
        }
    }

    /**
     * 获取key对应的缓存值,如果不存在则调用Loader进行加载,加载后的值不会存到缓存里,
     * 如需要存入缓存请使用{@link #get(String, org.moon.core.cache.memcached.Loader, int)}
     * @param key
     * @param loader
     * @return
     */
    public Object get(String key,Loader<Object> loader){
        Object result = get(key);
        if(Objects.isNull(result)){
            return loader.load();
        }
        return result;
    }

    /**
     * 获取key对应的缓存值,如果不存在则调用Loader进行加载,并将加载好的值存放到缓存里
     * @param key
     * @param loader
     * @param seconds
     * @return
     */
    public Object get(String key,Loader<Object> loader,int seconds){
        Object result = get(key);
        if(Objects.isNull(result)){
            result = loader.load();
            set(key, seconds, result);
        }
        return result;
    }

    /**
     * 获取key对应的缓存值，并转型为&lt;T&gt;
     * @param key
     * @param c
     * @param <T>
     * @return
     */
    public <T> T get(String key,Class<T> c){
        return (T)get(key);
    }

    /**
     * 获取key对应的缓存值，并转型为&lt;T&gt;,如果不存在则调用Loader进行加载
     * @param key
     * @param c
     * @param loader
     * @param <T>
     * @return
     */
    public <T> T get(String key,Class<T> c,Loader<T> loader){
        T t = (T)get(key);
        if(Objects.isNull(t)){
            return loader.load();
        }
        return t;
    }

    /**
     * 获取key对应的缓存值，并转型为&lt;T&gt;如果不存在则调用Loader进行加载,加载完毕后存入缓存
     * @param key
     * @param c
     * @param loader
     * @param seconds
     * @param <T>
     * @return
     */
    public <T> T get(String key,Class<T> c,Loader<T> loader,int seconds){
        T t = (T)get(key);
        if(Objects.isNull(t)){
            t = loader.load();
            set(key, seconds, t);
        }
        return t;
    }

    private void checkConnect(){
        if(Objects.nonNull(memcachedClient)){
            log.debug("reconnecting the memcached...");
            try {
                memcachedClient.get("checkStatus");
                stopTimer();
            }catch (Exception e){
                throw new ApplicationRunTimeException(e);
            }
        }else{
            connect();
        }
    }

    private void startTimer(){
        if(!connecting) {
            timer.schedule(task, 0, period);
        }
        connecting = true;
        available = false;
    }

    private void stopTimer(){
        connecting = false;
        timer.cancel();
        timer = null;
        timer = new Timer(true);
        try {//重置任务状态
            Field field = TimerTask.class.getDeclaredField("state");
            field.setAccessible(true);
            field.set(task, 0);
        } catch (Exception e) {
            throw new ApplicationRunTimeException(e);
        }
        available = true;
    }

    private void connect(){
        Map settingMap = systemSettingService.getSettingMap();
        Object openMemCachedFromDb = settingMap.get("memcached.open");
        if(Objects.nonNull(openMemCachedFromDb)){
            openMemCached = "true".equals(openMemCachedFromDb);
        }
        Object hostFromDb = settingMap.get("memcached.host");
        if(Objects.nonNull(hostFromDb)){
            hostName = (String)hostFromDb;
        }

        Object portFromDb = settingMap.get("memcached.port");
        if(Objects.nonNull(portFromDb)){
            portNum = Integer.parseInt((String)portFromDb);
        }
        if(openMemCached) {
            try {
                memcachedClient = new MemcachedClient(new InetSocketAddress(hostName, portNum));
                stopTimer();
            } catch (IOException e) {
                throw new ApplicationRunTimeException(e.getCause());
            }
        }
    }
    /**
     * 初始化memcached的客户端,当无法连接时,抛出{@link ApplicationRunTimeException}异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        connect();
    }

    public static void main(String[] args) throws InterruptedException {
        MemCachedManager m = new MemCachedManager();
        m.startTimer();
        Thread.sleep(10000);
    }
}
