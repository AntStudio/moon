package org.moon.base.domain.eventhandler;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.moon.base.domain.BaseDomain;
import org.moon.base.repository.CommonRepository;
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
public abstract class BaseEventHandler<T extends BaseDomain> implements BeanNameAware{

    @Resource
    private ConsumerLoader consumerLoader;
    
    @Resource
    private CommonRepository commonRepository;
    
    private String beanName;
    protected Logger logger  = Logger.getLogger(getClass());
    
    protected BaseEventHandler(){}
    
    
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
    	Class<T> c = (Class<T>) BaseDomain.class;
        registerHandlerForEvent("save",c);
        registerHandlerForEvent("delete",c);
        registerHandlerForEvent("update",c);
    }
    
    private void registerHandlerForEvent(String methodName,Class<T> paramType){
        String topicName = Strings.lowerFirst(getTClass().getSimpleName())+"/"+methodName;
        
        if(logger.isDebugEnabled()){
            logger.debug("Start regist handler for event{topicName:"+topicName+"}");
        }
        Map<String, Collection<ConsumerMethodHolder>> consumerMethods =  consumerLoader.getConsumerMethods();
        try{
            Method method = this.getClass().getMethod(methodName,paramType);
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
            logger.error("error register handler for event{topicName:"+topicName+"},caused by "+methodName+" not existed in "+this.getClass());
        }
    }
    
    public  T save(T domain){
    	domain.setId(commonRepository.save(domain));
    	return domain;
    }
    
    public void delete(T domain){
    	commonRepository.delete(getTClass(), new Long[]{domain.getId()});
    }
    
    public void update(T domain){
    	commonRepository.update(domain);
    }
    
    
}
