package org.moon.core.init.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface TableCreatorRepository {

    void createTableIfNecessary();

}
