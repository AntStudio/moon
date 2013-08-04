package com.greejoy.base.init.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface TableCreatorRepository {

	public void createTableIfNecessary();
	
}
