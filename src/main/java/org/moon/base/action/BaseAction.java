package org.moon.base.action;

import com.reeham.component.ddd.message.DomainMessage;
import com.reeham.component.ddd.model.ModelContainer;
import com.reeham.component.ddd.model.ModelUtils;
import org.moon.base.domain.BaseDomain;

import javax.annotation.Resource;

/**
 * base action, contains some common methods for the action layer
 * @author GavinCook
 * @since 1.0.0
 */
public class BaseAction{

    @Resource
    private ModelContainer modelContainer;
    
    /**
     * enhance domain object with the dependency
     * @param domain the domain
     * @return the enhanced domain
     */
    protected <T extends BaseDomain> T enhance(T domain){
        if(domain==null){
            return null;
        }
        domain = modelContainer.enhanceModel(domain);
        if(domain.getId()!=null){
            modelContainer.addModel(ModelUtils.asModelKey(domain.getClass(),domain.getId()), domain,false);
        }
        return domain;
    }

    /**
     * sync the domain message, this one will wait for the result of the domain message
     * @param domainMessage the domain message
     * @return the message result
     */
    protected Object sync(DomainMessage domainMessage){
    	return domainMessage.getEventResult();
    }
}
