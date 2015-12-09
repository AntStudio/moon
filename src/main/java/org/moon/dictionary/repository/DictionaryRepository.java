package org.moon.dictionary.repository;

import org.apache.ibatis.annotations.Param;
import org.moon.base.repository.BaseRepository;
import org.moon.dictionary.domain.Dictionary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DictionaryRepository extends BaseRepository<Dictionary>{

    List<Map<String,Object>> listChildrenByCode(@Param("code")String code);

    Long add(Map<String,Object> params);

    Map<String,Object> getDictionaryByCode(@Param("code")String code,@Param("parentId")Long parentId);

}
