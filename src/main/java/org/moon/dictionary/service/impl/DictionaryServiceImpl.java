package org.moon.dictionary.service.impl;

import org.moon.base.service.AbstractDomainService;
import org.moon.dictionary.domain.Dictionary;
import org.moon.dictionary.repository.DictionaryRepository;
import org.moon.dictionary.service.DictionaryService;
import org.moon.utils.Objects;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class DictionaryServiceImpl extends AbstractDomainService<Dictionary> implements DictionaryService {

    @Resource
    private DictionaryRepository dictionaryRepository;

    @Override
    public List<Map<String, Object>> listChildrenByCode(String code) {
        return dictionaryRepository.listChildrenByCode(code);
    }

    @Override
    public Long add(Map<String, Object> params) {
        return dictionaryRepository.add(params);
    }

    @Override
    public Map<String, Object> getDictionaryByCode(String code) {
        return dictionaryRepository.getDictionaryByCode(code,null);
    }

    @Override
    @Cacheable(value = "cache",key = "'dictionaryId'.concat(#code)")
    public Long getDictionaryIdByCode(String code) {
        Map<String,Object> dictionary = getDictionaryByCode(code);
        Object id = dictionary.get("id");
        try{
            return (Long)id;
        }catch (Exception e){
            return Long.parseLong(id+"");
        }
    }

    @Override
    public boolean isChild(String parentCode, String childCode) {
        if(Objects.isNull(childCode) || Objects.isNull(parentCode)){
            return false;
        }
        List<Map<String,Object>> dictionaries = listChildrenByCode(parentCode);
        for(Map<String,Object> dictionary : dictionaries){
            if(childCode.equals(dictionary.get("code"))){
                return true;
            }
        }
        return false;
    }
}
