package org.moon.rbac.service.impl;
/*package com.greejoy.rbac.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.greejoy.rbac.repository.BaseRepository;
import com.greejoy.rbac.service.BaseService;
import com.reeham.component.ddd.model.ModelContainer;
import com.reeham.component.ddd.model.ModelLoader;
import com.reeham.component.ddd.model.ModelUtils;

public class AbstractService<T,R extends BaseRepository<T>> implements BaseService<T>{

	@Override
	public void save(T o) {
		 	
	}

	@Override
	public Integer update(T o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long[] ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long[] ids, boolean logicDel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id, boolean logicDel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T getModel(Long id) {
		return (T) modelContainer.getModel(ModelUtils.asModelKey(this.getClass(), id),domainModelLoader);
	}
	
	protected ModelLoader domainModelLoader =  new ModelLoader() {
        @Override
        public Object loadModel(Object id) {
            return get((Long)id);
        }
    };

	@Override
	public T get(Long id) {
	 
		return baseRepository.get(id);
	}

}
*/