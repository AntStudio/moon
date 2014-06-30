package org.moon.base.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.moon.base.repository.CommonRepository;
import org.moon.core.orm.mybatis.Criteria;
import org.moon.core.orm.mybatis.DataConverter;
import org.moon.core.orm.mybatis.criterion.Restrictions;
import org.moon.pagination.Pager;
import org.moon.utils.Dtos;
import org.moon.utils.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.reeham.component.ddd.model.ModelContainer;
import com.reeham.component.ddd.model.ModelLoader;
import com.reeham.component.ddd.model.ModelUtils;

/**
 * 抽象的服务类，封装了一些通用的仓储类调用
 * @author Gavin
 * @date Jun 9, 2014
 */

public abstract class AbstractService<T> implements BaseService<T>,ModelLoader{

	@Resource
	private CommonRepository repository;
	
	@Resource
	public ModelContainer  modelContainer;
	
	private Logger logger = LoggerFactory.getLogger(getGeneric());
	
	protected AbstractService(){}
	
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
                     + AbstractService.class.getName());
		}
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
	public T get(Long id) {
		return (T) modelContainer.getModel(ModelUtils.asModelKey(getGeneric(), id),this);
	}
	
    @Override
    public Object loadModel(Object identifier) {
        return load((Long) identifier);
    }
    
	@Override
	public T load(Long id){
		System.out.println(ModelUtils.asModelKey(getGeneric(), id));
		Criteria criteria = new Criteria();
		criteria.add(Restrictions.eq("id", id)).limit(1);
		List<Map> list = repository.list(getGeneric(),criteria);
		T instance  = null;
		Class c = getGeneric();
		if(list.size()>0){
			try{
				Map m = list.get(0);
				instance = (T) c.newInstance();
				Class<?> type;
	    		Object value;
	    		String fieldName ;
				for(Object key:m.keySet()){
					fieldName = (String)key;
					value = m.get(key);
					try{
						Field f = c.getDeclaredField(Strings.changeUnderlineToCamelBak(fieldName));
						if(f!=null){
							f.set(instance, value);
						}
					}catch (Exception e) {//当字段不存在，或者为父类私有字段时，寻找是否有相应的Setter方法
						logger.debug("Trying use setter to set value for field "+fieldName);
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
								} else if (type ==boolean.class&&type==Boolean.class){
									try {
										method.invoke(instance, Boolean.parseBoolean(value.toString()));
									} catch (Exception exception) {
										method.invoke(instance, false);
									}
								} else method.invoke(instance, value);
	    						continue;
	    					}
	    				}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			throw new IllegalStateException("Can't load "+c+" for [id="+id+"]");
		}
		return instance ;
	}
	
	@Override
	public Pager listForPage(Criteria criteria) {
		Class c = getGeneric();
		List<T> results = modelContainer.identifiersToModels((List)repository.listIds(c, criteria), getGeneric(), this);
		return new Pager(count(criteria),(List)results,criteria.getPageSize(), criteria.getPageIndex());
	}
	
	@Override
	public Pager listForPage(Criteria criteria, DataConverter<T> dataConverter) {
		Class c = getGeneric();
		List<T> results = modelContainer.identifiersToModels((List)repository.listIds(c, criteria), getGeneric(), this);
		return new Pager(count(criteria),Dtos.covert(results,dataConverter),criteria.getPageSize(), criteria.getPageIndex());
	}
	
	private int count(Criteria criteria){
		return repository.count(getGeneric(), criteria);
	}
	
	@Override
	public void delete(Long[] ids) {
		Assert.notEmpty(ids,"Can't delete for null ids");
		for(Long id:ids){
			modelContainer.removeModel(ModelUtils.asModelKey(getGeneric(), id));
		}
		repository.delete(getGeneric(), ids);
	}
}
