package org.moon.dictionary.repository;

import org.moon.base.repository.BaseRepository;
import org.moon.dictionary.domain.Dictionary;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryRepository extends BaseRepository<Dictionary>{
	
}
