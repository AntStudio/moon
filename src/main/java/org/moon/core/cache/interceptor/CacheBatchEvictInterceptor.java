package org.moon.core.cache.interceptor;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.moon.core.cache.annotation.CacheBatchEvict;
import org.moon.utils.Objects;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * {@link CacheBatchEvict}的拦截器
 * <p>根据#{@link org.moon.core.cache.annotation.CacheBatchEvict#value()}所配置的缓存名字以及
 * #{@link org.moon.core.cache.annotation.CacheBatchEvict#key()} 所生成的key数组依次遍历删除每一个
 * #{@link org.moon.core.cache.annotation.CacheBatchEvict#value()}对应缓存的对应key的缓存
 * @author GavinCook
 * @since 1.0.0
 * @see org.moon.utils.Strings#concatPrefix(String, Object[])
 * @see org.moon.core.cache.annotation.CacheBatchEvict
 */
@Component
public class CacheBatchEvictInterceptor implements MethodInterceptor {

    private final Pattern pattern = Pattern.compile("#(\\w+)");

    private final ExpressionParser parser = new SpelExpressionParser();

    @Resource
    private CacheManager cacheManager;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        CacheBatchEvict cacheBatchEvict = method.getAnnotation(CacheBatchEvict.class);
        if(Objects.nonNull(cacheBatchEvict)) {
            //根据cache名字获取cache对象数组
            String[] cacheNames = cacheBatchEvict.value();
            int cacheLength = cacheNames.length;
            Cache[] caches = new Cache[cacheLength];
            for (int l = 0; l < cacheLength; l++) {
                caches[l] = cacheManager.getCache(cacheNames[l]);
            }
            //将方法参数封装为Map
            Map<String,Object> paramsMap = new HashMap<String, Object>();
            int i = 0;
            Object[] params = methodInvocation.getArguments();
            //需要使用本地变量表获取参数名字，否则获取到的参数名字为arg0,arg1等
            LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
            for(String name : discoverer.getParameterNames(method)){
                paramsMap.put(name,params[i]);
                i++;
            }
            //解析key的spring EL表达式，并遍历删除cacheKey
            String[] cacheKeys = getCacheKeys(cacheBatchEvict.key(), paramsMap);
            for (String cacheKey : cacheKeys) {
                for (int l = 0; l < cacheLength; l++) {
                    caches[l].evict(cacheKey);
                }
            }
        }
        return methodInvocation.proceed();
    }

    /**
     * 解析key(spring EL)，返回缓存key数组
     * @param keyString
     * @param paramsMap
     * @return
     */
    private String[] getCacheKeys(String keyString,Map<String,Object> paramsMap){
        String parserString =  pattern.matcher(keyString).replaceAll("[$1]");//将#xxx转换为[xxx]，因为参数将组装为map,需要[xxx]来获取
        return parser.parseExpression(parserString).getValue(paramsMap,String[].class);
    }
}
