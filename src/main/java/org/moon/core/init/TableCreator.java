package org.moon.core.init;

import org.apache.log4j.Logger;
import org.moon.db.manager.DBManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import javax.annotation.Resource;



public class TableCreator implements BeanFactoryAware{
	private Logger log = Logger.getLogger(getClass());
	@Resource
	private DBManager dbManager;
	@Override
	public void setBeanFactory(BeanFactory arg0) throws BeansException {
		log.debug("[spring-rbac]create platform table if necessary");
		dbManager.createTableIfNecessary();
		log.info("[spring-rbac]create platform table successfully.");
	}

}
