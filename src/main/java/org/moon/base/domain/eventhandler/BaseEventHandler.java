package org.moon.base.domain.eventhandler;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.moon.utils.Strings;
import org.springframework.beans.factory.BeanNameAware;

import com.reeham.component.ddd.message.disruptor.consumer.ConsumerLoader;
import com.reeham.component.ddd.message.disruptor.consumer.ConsumerMethodHolder;

/**
 * 通用的事件处理,Domain的EventHandler可以直接继承此类，从而省去相关的save,update,delete事件的处理。
 * 默认注册主题为：类名(首字母小写)+"/"+[save,update,delete]
 * @author Gavin
 * @Date 2013-12-30
 */
public abstract class BaseEventHandler<T> implements BeanNameAware{

    @Resource
    private ConsumerLoader consumerLoader;
    private String beanName;
    protected Logger logger  = Logger.getLogger(getClass());
    
    @SuppressWarnings("unchecked")
	private Class<T> getTClass(){
      return (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
        registerHandlerForGenericEvent();
    }
    
    /**
     * 注册通用的事件处理器,包括save,update,delete
     */
    private void registerHandlerForGenericEvent(){
        registerHandlerForEvent("save",getTClass());
        registerHandlerForEvent("delete",getTClass());
        registerHandlerForEvent("update",getTClass());
    }
    
    private void registerHandlerForEvent(String methodName,Class<T> paramType){
        String topicName = Strings.lowerFirst(getTClass().getSimpleName())+"/"+methodName;
        
        if(logger.isDebugEnabled()){
            logger.debug("Start regist handler for event{topicName:"+topicName+"}");
        }
        Map<String, Collection<ConsumerMethodHolder>> consumerMethods =  consumerLoader.getConsumerMethods();
        try{
            Method method = this.getClass().getDeclaredMethod(methodName,paramType);
            Collection<ConsumerMethodHolder> methods = consumerMethods.get(topicName);
            if (methods == null) {
                methods = new LinkedList<ConsumerMethodHolder>();
                consumerMethods.put(topicName, methods);
            }
            methods.add(new ConsumerMethodHolder(beanName, method));
            if(logger.isDebugEnabled()){
                logger.debug("Success regist handler for event{topicName:"+topicName+",method:"+method+"}");
            }
        }catch(NoSuchMethodException e){
            logger.error("error register handler for event{topicName:"+topicName+"},caused by "+methodName+" not existed in "+getTClass());
        }
    }
    
    /******************** 子类必须复写的方法 ********************/
    
    public abstract T save(T domain);
    public abstract void delete(T domain);
    public abstract void update(T domain);
    
    /******************** /子类必须复写的方法 ********************/
    
}
