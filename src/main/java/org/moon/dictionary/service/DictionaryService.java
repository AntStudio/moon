package org.moon.dictionary.service;

import org.moon.base.service.BaseDomainService;
import org.moon.dictionary.domain.Dictionary;

import java.util.List;
import java.util.Map;

public interface DictionaryService extends BaseDomainService<Dictionary> {

    /**
     * get the sub dictionaries according to the dictionary code
     * @param code the parent dictionary code
     * @return sub dictionaries
     */
    List<Map<String,Object>> listChildrenByCode(String code);

    /**
     * add dictionary, this way would not use domain way. mainly used when the system startup
     * @param params the dictionary
     * @return the dictionary ID
     */
    Long add(Map<String,Object> params);

    /**
     * get the dictionary according the dictionary code
     * @param code the dictionary code
     * @return the dictionary if there has corresponding one
     */
    Map<String,Object> getDictionaryByCode(String code);

    /**
     * get the dictionary ID according the dictionary code
     * @param code the dictionary code
     * @return the dictionary ID
     */
    Long getDictionaryIdByCode(String code);

    /**
     * check if the dictionary stands by <code>childCode</code> is the belongs the dictionary stands by
     * <code>parentCode</code>
     * @param parentCode the parent dictionary code
     * @param childCode the child dictionary code
     * @return <code>true</code> if the <code>childCode</code> is one of the sub dictionaries of the parent dictionary,
     * Or return <code>false</code>
     */
    boolean isChild(String parentCode, String childCode);
}
