package org.moon.base.domain;

import com.reeham.component.ddd.annotation.Model;
import com.reeham.component.ddd.message.DomainMessage;
import com.reeham.component.ddd.message.EventMessageFirer;
import com.reeham.component.ddd.model.ModelContainer;
import com.reeham.component.ddd.model.ModelUtils;
import org.moon.core.annotation.NoLogicDeleteSupport;
import org.moon.core.orm.mybatis.annotation.IgnoreNull;
import org.moon.core.spring.ApplicationContextHelper;
import org.moon.exception.ApplicationRunTimeException;
import org.moon.utils.Strings;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Objects;

/**
 * the base domain, like a common events sender. contains the <code>save,update,delete</code> topic event sender.
 * except this, also define some common properties, like <code>id</code>.generally, all domain should extend this one.
 * @author GavinCook
 * @since 1.0.0
 */
public class BaseDomain implements Serializable {

    @Resource
    private transient EventMessageFirer eventMessageFirer;

    @Resource
    private transient ModelContainer modelContainer;

    private static final long serialVersionUID = 3121315303319780752L;

    /**
     * the identifier of domain
     */
    @IgnoreNull
    protected Long id;

    /**
     * delete flag, use for logically delete. default false.
     */
    protected boolean deleteFlag = false;

    public BaseDomain() {}

    public BaseDomain(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * if current domain type support the logic delete. simply check the class is annotated with
     * {@link NoLogicDeleteSupport} or not
     * @return <code>true</code> if supported, Or return <code>false</code>
     * @see NoLogicDeleteSupport
     */
    public boolean supportLogicDelete() {
        return !this.getClass().isAnnotationPresent(NoLogicDeleteSupport.class);
    }


    /**
     * persist the domain, notice: this operation just fire the event and handle it async. if need wait it done, can use
     * {@link DomainMessage#getResultEvent()}
     * @return the domain message
     * @see org.moon.base.action.BaseAction#sync(DomainMessage)
     * @see #sync(DomainMessage)
     */
    public DomainMessage save() {
        enhanceIfNecessary();
        DomainMessage dm = new DomainMessage(this);
        eventMessageFirer.fireDisruptorEvent(Strings.lowerFirst(getDomainClass().getSimpleName()) + "/save", dm);
        return dm;
    }

    /**
     * update persisted domain, notice: this operation just fire the event and handle it async. if need wait it done, can use
     * {@link DomainMessage#getResultEvent()}
     * @return the domain message
     * @see org.moon.base.action.BaseAction#sync(DomainMessage)
     * @see #sync(DomainMessage)
     */
    public DomainMessage update() {
        enhanceIfNecessary();
        DomainMessage dm = new DomainMessage(this);
        eventMessageFirer.fireDisruptorEvent(Strings.lowerFirst(getDomainClass().getSimpleName()) + "/update", dm);
        modelContainer.removeModel(ModelUtils.asModelKey(this.getClass(), this.getId()));
        return dm;
    }

    /**
     * delete persisted domain, notice: this operation just fire the event and handle it async. if need wait it done, can use
     * {@link DomainMessage#getResultEvent()}
     * @return the domain message
     * @see org.moon.base.action.BaseAction#sync(DomainMessage)
     * @see #sync(DomainMessage)
     */
    public DomainMessage delete() {
        enhanceIfNecessary();
        DomainMessage dm = new DomainMessage(this);
        eventMessageFirer.fireDisruptorEvent(Strings.lowerFirst(getDomainClass().getSimpleName()) + "/delete", dm);
        modelContainer.removeModel(ModelUtils.asModelKey(this.getClass(), getId()));
        return dm;
    }

    /**
     * delete persisted domain, notice: this operation just fire the event and handle it async. if need wait it done, can use
     * {@link DomainMessage#getResultEvent()}
     * @param logicDelete if <code>true</code>, would just update the delete flag to <code>true</code>, OR would delete it
     * @return the domain message
     * @see org.moon.base.action.BaseAction#sync(DomainMessage)
     * @see #sync(DomainMessage)
     */
    public DomainMessage delete(boolean logicDelete) {
        if (logicDelete) {
            this.setDeleteFlag(true);
            return update();
        } else {
            return delete();
        }
    }

    /**
     * make the event handler in sync operation
     * @param domainMessage the domain message
     * @return the actual result returned by the event handler
     */
    public Object sync(DomainMessage domainMessage) {
        return domainMessage.getEventResult();
    }

    /**
     * enhance the domain modal for the dependency if necessary
     */
    public void enhanceIfNecessary() {
        if (this.eventMessageFirer == null) {
            ApplicationContextHelper.getBean(ModelContainer.class).enhanceModel(this);
            if (this.getId() != null) {
                modelContainer.addModel(ModelUtils.asModelKey(this.getClass(), this.getId()), this, false);
            }
        }
    }

    /**
     * get the domain class, which is annotated on the {@link Model}, else would find the parent class util find matched one.
     * and would throw exception if no class annotated {@link Model}. it's useful multiple extends for one domain class.
     * in the {@link org.moon.core.orm.mybatis.SQLProvider} will use this to determine which class metadata should use to persist data.
     * the specific domain class can override this method.
     * @return the domain class
     */
    protected Class<?> getDomainClass() {
        Class<?> targetClass = this.getClass();
        while (Objects.isNull(targetClass.getDeclaredAnnotation(Model.class))) {
            targetClass = targetClass.getSuperclass();
            if (targetClass == Object.class) {
                throw new ApplicationRunTimeException("neither " + this.getClass().getName() + " nor it's super class is annotated by Model annotation");
            }
        }
        return targetClass;
    }
}
