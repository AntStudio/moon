package org.moon.base.domain;

import com.reeham.component.ddd.message.DomainMessage;
import com.reeham.component.ddd.message.EventMessageFirer;
import com.reeham.component.ddd.model.ModelContainer;
import com.reeham.component.ddd.model.ModelUtils;
import org.moon.core.annotation.NoLogicDeleteSupport;
import org.moon.core.spring.ApplicationContextHelper;
import org.moon.utils.Strings;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * 域模型的基本对象,包含了一些公共基本属性,如:
 * <pre>id</pre>
 * 也包含了领域的公共方法，如：
 * <pre>save,update,delete</pre>
 * 一般的域模型都会继承于 {@link org.moon.base.domain.BaseDomain}
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27 
 */
public class BaseDomain implements Serializable{

    @Resource
    private EventMessageFirer eventMessageFirer;
    @Resource
    private ModelContainer modelContainer;
	/**
	 * 版本序列标示
	 */
	private static final long serialVersionUID = 3121315303319780752L;

	/**
	 * 域模型对象的id
	 */
	protected Long id;
	
	/**
	 * 如果 <code>deleteFlag 为 true;</code>,则表示删除
	 */
	protected boolean deleteFlag = false;

	public BaseDomain(){}
	
	public BaseDomain(Long id){
		this.id = id;
	}
	
	/**
	 * @return 返回id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param 设置id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return 返回删除标示
	 */
	public boolean isDeleteFlag() {
		return deleteFlag;
	}

	/**
	 * @param 设置删除标示
	 */
	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	
	public boolean supportLogicDelete(){
		return !this.getClass().isAnnotationPresent(NoLogicDeleteSupport.class);
	}
	/******************** 领域方法  ********************/
	
	/**
	 * 持久化领域,如果需要等待结果可使用 {@link com.reeham.component.ddd.message.DomainMessage#getResultEvent()}
	 * @return
	 */
	public DomainMessage save(){
		enhanceIfNecessary();
	    DomainMessage dm = new DomainMessage(this);
	    eventMessageFirer.fireDisruptorEvent(Strings.lowerFirst(this.getClass().getSimpleName())+"/save",dm);
	    return dm;
	}
	
	/**
     * 更新领域持久化数据,如果需要等待结果可使用 {@link com.reeham.component.ddd.message.DomainMessage#getResultEvent()}
     * @return
     */
	public DomainMessage update(){
		enhanceIfNecessary();
	    DomainMessage dm = new DomainMessage(this);
        eventMessageFirer.fireDisruptorEvent(Strings.lowerFirst(this.getClass().getSimpleName())+"/update",dm);
        modelContainer.removeModel(ModelUtils.asModelKey(this.getClass(), this.getId()));
        return dm;
	}
   
	/**
     * 删除领域对应的持久化数据(物理删除)
     * @return
     */
    public DomainMessage delete(){
    	enhanceIfNecessary();
        DomainMessage dm = new DomainMessage(this);
        eventMessageFirer.fireDisruptorEvent(Strings.lowerFirst(this.getClass().getSimpleName())+"/delete",dm);
        modelContainer.removeModel(ModelUtils.asModelKey(this.getClass(), getId()));
        return dm;
    }
	
    /**
     * 删除领域对应的持久化数据,根据传入的参数进行逻辑删除或者物理删除
     * @return
     */
    public DomainMessage delete(boolean logicDelete){
        if(logicDelete){
            this.setDeleteFlag(true);
            return update();
        }else{
            return delete();
        }
    }
    
    /**
     * 同步执行
     * @param domainMessage
     * @return
     */
    public Object sync(DomainMessage domainMessage){
    	return domainMessage.getResultEvent();
    }
    
    public void enhanceIfNecessary(){
    	if(this.eventMessageFirer==null){
    		ApplicationContextHelper.getBean(ModelContainer.class).enhanceModel(this);
    		if(this.getId()!=null){
                modelContainer.addModel(ModelUtils.asModelKey(this.getClass(),this.getId()), this,false);
            }
    	}
    }
	/******************** /领域方法  ********************/
}
