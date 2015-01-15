package org.moon.dictionary.service.impl;

import org.moon.base.service.AbstractDomainService;
import org.moon.dictionary.domain.Dictionary;
import org.moon.dictionary.repository.DictionaryRepository;
import org.moon.dictionary.service.DictionaryService;
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
    public Map<String, Object> getDictionaryByCode(Map<String, Object> params) {
        return dictionaryRepository.getDictionaryByCode(params);
    }
}
