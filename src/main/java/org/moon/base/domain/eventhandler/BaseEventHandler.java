package org.moon.base.domain.eventhandler;

import com.reeham.component.ddd.message.disruptor.consumer.ConsumerLoader;
import com.reeham.component.ddd.message.disruptor.consumer.ConsumerMethodHolder;
import org.apache.log4j.Logger;
import org.moon.base.domain.BaseDomain;
import org.moon.base.repository.CommonRepository;
import org.moon.base.service.BaseDomainService;
import org.moon.utils.Strings;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * 通用的事件处理,Domain的EventHandler可以直接继承此类，从而省去相关的save,update,delete,get事件的处理。
 * 默认注册主题为：类名(首字母小写)+"/"+[save,update,delete,get]
 * @author Gavin
 * @Date 2013-12-30
 */
public abstract class BaseEventHandler<T extends BaseDomain,K extends BaseDomainService<T>> implements ApplicationContextAware,BeanNameAware{

    @Resource
    private ConsumerLoader consumerLoader;
    
    @Resource
    private CommonRepository commonRepository;
    
    protected K service;
    
    private String beanName;
    protected Logger logger  = Logger.getLogger(getClass());
    
    protected BaseEventHandler(){}
    
    
	private Class<T> getTClass(){
      return (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
	private Class<K> getKClass(){
	      return (Class<K>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	    }
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.service  = applicationContext.getBean(getKClass());
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
        registerHandlerForEvent("get",Long.class);
    }
    
    private void registerHandlerForEvent(String methodName,Class<?> paramType){
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
    
    public  T get(Long id){
    	return service.get(id);
    }
    
    public void delete(T domain){
    	commonRepository.delete(getTClass(), new Long[]{domain.getId()});
    }
    
    public void update(T domain){
    	commonRepository.update(domain);
    }
    
    
}
