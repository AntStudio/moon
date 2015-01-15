package org.moon.dictionary.repository;

import org.apache.ibatis.annotations.Param;
import org.moon.base.repository.BaseRepository;
import org.moon.dictionary.domain.Dictionary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DictionaryRepository extends BaseRepository<Dictionary>{

    public List<Map<String,Object>> listChildrenByCode(@Param("code") String code);

    public Long add(Map<String, Object> params);

    /**
     * 根据字典code获取字典
     * @param params
     * @return
     */
    public Map<String,Object> getDictionaryByCode(Map<String, Object> params);

}
