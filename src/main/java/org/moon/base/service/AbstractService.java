package org.moon.base.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.moon.base.repository.BaseRepository;
import org.moon.base.repository.CommonRepository;
import org.moon.core.orm.mybatis.Criteria;
import org.moon.core.orm.mybatis.criterion.Restrictions;
import org.moon.core.spring.ApplicationContextHelper;
import org.springframework.util.Assert;

/**
 * 抽象的服务类，封装了一些通用的仓储类调用
 * @author Gavin
 * @date Jun 9, 2014
 */

public abstract class AbstractService<T> implements BaseService<T>{

	private BaseRepository<T> repository = ApplicationContextHelper.getBean(CommonRepository.class);

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
	public T load(Long id){
		Criteria criteria = new Criteria();
		criteria.add(Restrictions.eq("id", id)).limit(1);
		return (T) repository.list(getGeneric(),criteria);
	}
	
	
}
