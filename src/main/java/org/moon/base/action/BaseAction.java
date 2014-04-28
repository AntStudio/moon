package org.moon.base.action;

import javax.annotation.Resource;

import org.moon.base.domain.BaseDomain;

import com.reeham.component.ddd.model.ModelContainer;
import com.reeham.component.ddd.model.ModelUtils;

/**
 * @author Gavin
 * @version 1.0
 * @Date 2014-01-06
 */
public class BaseAction{

    @Resource
    private ModelContainer modelContainer;
    
    /**
     * 增强领域模型，并且将领域对象添加到缓存
     * @param domain
     * @return
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
}
