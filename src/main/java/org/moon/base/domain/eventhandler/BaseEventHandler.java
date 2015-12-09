package org.moon.base.domain.eventhandler;

import com.reeham.component.ddd.message.disruptor.consumer.ConsumerLoader;
import com.reeham.component.ddd.message.disruptor.consumer.ConsumerMethodHolder;
import com.reeham.component.ddd.model.ModelLoader;
import org.apache.log4j.Logger;
import org.moon.base.domain.BaseDomain;
import org.moon.base.repository.CommonRepository;
import org.moon.base.service.BaseDomainService;
import org.moon.exception.ApplicationRunTimeException;
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
 * <p>
 *     common event handler, the specific domain handler which extend from this one, can omit the
 *     <code>save,update,delete,get</code> event handler. the default event topic format:
 *     class name(first letter lowercase)+"/"+[save,update,delete,get].
 * </p>
 * <p>
 *     common event handler need two parameter type, the first one is the specific domain which should extend from {@link BaseDomain},
 *     ie:{@link org.moon.rbac.domain.User}. the second one is the specific service which should extend from {@link BaseDomainService},
 *     it mainly used to act as {@link ModelLoader} to get or load the domain, will get from the spring container
 * </p>
 * @author GavinCook
 * @since 1.0.0
 */
public abstract class BaseEventHandler<T extends BaseDomain,K extends BaseDomainService<T>>
        implements ApplicationContextAware,BeanNameAware{

    @Resource
    private ConsumerLoader consumerLoader;
    
    @Resource
    private CommonRepository<T> commonRepository;
    
    protected K service;
    
    private String beanName;

    protected Logger logger  = Logger.getLogger(getClass());
    
    protected BaseEventHandler(){}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.service = applicationContext.getBean(getKClass());
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
        registerHandlerForGenericEvents();
    }

    
    public T save(T domain){
        commonRepository.save(domain);
        return domain;
    }

    public T get(Long id) {
        return service.get(id);
    }

    public void delete(T domain) {
        commonRepository.delete(getTClass(), new Long[]{domain.getId()});
    }

    public void update(T domain) {
        commonRepository.update(domain);
    }

    @SuppressWarnings("unchecked")
    private Class<T> getTClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    private Class<K> getKClass() {
        return (Class<K>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    /**
     * register common event handler for [save, update, delete]
     */
    private void registerHandlerForGenericEvents(){
        Class<?> c = BaseDomain.class;
        registerHandlerForEvent("save",c);
        registerHandlerForEvent("delete",c);
        registerHandlerForEvent("update",c);
        registerHandlerForEvent("get",Long.class);
    }

    /**
     * register the event handler with method name and the parameter type, actually, will bind the matched method
     * on the topic <code>Strings.lowerFirst(getTClass().getSimpleName())+"/"+methodName</code>,ie:<code>user/save</code>
     * for {@link org.moon.rbac.domain.User} save operation
     * @param methodName the method name
     * @param paramType the parameter type of method
     */
    private void registerHandlerForEvent(String methodName,Class<?> paramType){
        String topicName = Strings.lowerFirst(getTClass().getSimpleName())+"/"+methodName;

        if(logger.isDebugEnabled()){
            logger.debug("Start register handler for event {topicName:"+topicName+"}");
        }
        Map<String, Collection<ConsumerMethodHolder>> consumerMethods =  consumerLoader.getConsumerMethods();
        try{
            Method method = this.getClass().getMethod(methodName,paramType);
            Collection<ConsumerMethodHolder> methods = consumerMethods.get(topicName);
            if (methods == null) {
                methods = new LinkedList<>();
                consumerMethods.put(topicName, methods);
            }
            methods.add(new ConsumerMethodHolder(beanName, method));
            if(logger.isDebugEnabled()){
                logger.debug("Success register handler for event {topicName:"+topicName+",method:"+method+"}");
            }
        }catch(NoSuchMethodException e){
            logger.error("error register handler for event {topicName:"+topicName+"},caused by "+methodName+" not existed in "+this.getClass());
            throw new ApplicationRunTimeException(e);
        }
    }
}
