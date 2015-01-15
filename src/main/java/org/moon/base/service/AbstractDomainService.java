package org.moon.base.service;

import com.reeham.component.ddd.model.ModelContainer;
import com.reeham.component.ddd.model.ModelUtils;
import org.moon.base.domain.BaseDomain;
import org.moon.base.repository.CommonRepository;
import org.moon.core.domain.DomainLoader;
import org.moon.core.orm.mybatis.Criteria;
import org.moon.core.orm.mybatis.DataConverter;
import org.moon.core.orm.mybatis.criterion.Restrictions;
import org.moon.pagination.Pager;
import org.moon.utils.*;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 抽象的服务类，封装了一些通用的仓储类调用
 * @author Gavin
 * @date Jun 9, 2014
 */

public abstract class AbstractDomainService<T extends BaseDomain> extends AbstractService implements BaseDomainService<T>,BeanNameAware{

	@Resource
	private CommonRepository repository;
	
	@Resource
	public ModelContainer  modelContainer;
	
	@Resource
	private DomainLoader domainLoader;

    @Resource
    private SqlSessionFactoryBean sqlSessionFactoryBean;

	private Logger logger = LoggerFactory.getLogger(getGeneric());
	
	protected AbstractDomainService(){
	}
	
	/**
	 * 获取泛型类型
	 * @return
	 */
	public Class getGeneric(){
		Type[] types = ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments();
		Assert.notEmpty(types);
		if(types[0] instanceof Class){
			return (Class) types[0];
		}else{
			 throw new IllegalStateException("concrete class " + getClass().getName()
                     + " must have a generic binding for "
                     + AbstractDomainService.class.getName());
		}
	}
	
	@Override
	public List<T> listForDomain(Criteria criteria) {
		Class c = getGeneric();
		return modelContainer.identifiersToModels((List)repository.listIds(c, criteria), getGeneric(), this);
	}
	
	@Override
	public T get(Long id) {
		return (T) modelContainer.getModel(ModelUtils.asModelKey(getGeneric(), id),this);
	}
	
    @Override
    public Object loadModel(Object identifier) {
        return load((Long) identifier);
    }
    
	@Override
	public T load(Long id){
		Criteria criteria = new Criteria();
		criteria.add(Restrictions.eq("id", id)).limit(1);
		List<Map> list = repository.list(getGeneric(),criteria);
		T instance  = null;
		Class c = getGeneric();
        Object value = null;
        String fieldName = null;
		if(list.size()>0){
			try{
				Map m = list.get(0);
				instance = (T) c.newInstance();
				Class<?> type;
                Field f = null;
				for(Object key:m.keySet()){
					fieldName = Strings.changeUnderlineToCamelBak((String)key);
					value = m.get(key);
					try{
                        f = c.getDeclaredField(fieldName);
                        f.setAccessible(true);
						if(f!=null){
                            logger.debug("Trying set value for field "+fieldName);
							f.set(instance, value);
                            logger.debug("Success set value for field "+fieldName);
						}
					}catch (IllegalArgumentException ae){//当字段类型不匹配时
                        if(f.getType().equals(String.class)&&value instanceof byte[]){
                            f.set(instance, new String((byte[])value,"UTF-8"));
                        }else if(f.getType().equals(Long.class)){
                            f.set(instance,Long.parseLong(value+""));
                        }else{
                            ae.printStackTrace();
                        }
                    }catch (Exception e) {//当字段不存在，或者为父类私有字段时，寻找是否有相应的Setter方法
						logger.debug("Trying use setter to set value for field {},the value is:{}",fieldName,value);
	    				for(Method method:c.getMethods()){
	    					if(method.getName().equals("set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1))
	    							&&method.getParameterTypes().length==1)
	    					{	
	    						type=method.getParameterTypes()[0];
	    						//待增加属性为自定义类型(如：classroom.student.name,classroom.student.no)&&已经传入列表和数据等类型
								if (type == int.class || type == Integer.class) {
									try {
										method.invoke(instance, Integer.parseInt(value.toString()));
									} catch (Exception exception) {
										method.invoke(instance, 0);
									}
								} else if (type == long.class || type == Long.class) {
									try {
										method.invoke(instance, Long.parseLong(value.toString()));
									} catch (Exception exception) {
										method.invoke(instance, 0L);
									}
								} else if (type == double.class || type == Double.class) {
									try {
										method.invoke(instance, Double.parseDouble(value.toString()));
									} catch (Exception exception) {
										method.invoke(instance, 0.00);
									}
								} else if (type == float.class || type == Float.class) {
									try {
										method.invoke(instance, Float.parseFloat(value.toString()));
									} catch (Exception exception) {
										method.invoke(instance, 0.0);
									}
								} else if (type ==boolean.class||type==Boolean.class){
									try {
										method.invoke(instance, Boolean.parseBoolean(value.toString()));
									} catch (Exception exception) {
										method.invoke(instance, false);
									}
								} else {
									method.invoke(instance, value);
								}
	    						break;
	    					}
	    				}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
                logger.debug("Trying use setter to set value for field {},the value is:{}",fieldName,value);
            }
		}else{
			throw new IllegalStateException("Can't load "+c+" for [id="+id+"]");
		}
		return instance ;
	}

    @Override
    public List<Map> list(){
        return repository.list(getGeneric(),null);
    }

    @Override
    public List<Map> list(Criteria criteria){
        return repository.list(getGeneric(),criteria);
    }

	@Override
	public Pager listForPage(Criteria criteria) {
        return new Pager(count(criteria),(List)list(criteria),criteria.getPageSize(), criteria.getPageIndex());
	}

    @Override
	public Pager listForPage(Criteria criteria, DataConverter<Map> dataConverter) {
        List<Map> results = list(criteria);
        return new Pager(count(criteria),Dtos.convert(results, dataConverter),criteria.getPageSize(), criteria.getPageIndex());
	}
	
	@Override
	public void delete(Long[] ids,boolean logicFlag) {
		Assert.notEmpty(ids,"Can't delete for null ids");
		if(logicFlag){//逻辑删除
			for(Long id:ids){
				T t = get(id);
				t.setDeleteFlag(true);
				if(t.supportLogicDelete()){
					t.update();
				}
			}
		}else{//物理删除
			for(Long id:ids){
				modelContainer.removeModel(ModelUtils.asModelKey(getGeneric(), id));
			}
			repository.delete(getGeneric(), ids);
		}
	}
	
	@Override
	public <K> K loadDomain(Class<K> c, Long id) {
		return domainLoader.load(c, id);
	}
	private int count(Criteria criteria){
		return repository.count(getGeneric(), criteria);
	}
	
	@Override
	public void setBeanName(String name) {
		domainLoader.registerDomainLoader(getGeneric(), this);
	}
}
