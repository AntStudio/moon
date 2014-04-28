package org.moon.base.init.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface TableCreatorRepository {

	public void createTableIfNecessary();
	
}
