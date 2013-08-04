package com.greejoy.base.init;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.greejoy.base.init.repository.TableCreatorRepository;


public class TableCreator implements BeanFactoryAware{
	private Logger log = Logger.getLogger(getClass());
	@Resource
	private TableCreatorRepository tableCreatorRepository;
	@Override
	public void setBeanFactory(BeanFactory arg0) throws BeansException {
		log.debug("[spring-rbac]create platform table if necessary");
		tableCreatorRepository.createTableIfNecessary();
		log.info("[spring-rbac]create platform table successfully.");
	}

}
