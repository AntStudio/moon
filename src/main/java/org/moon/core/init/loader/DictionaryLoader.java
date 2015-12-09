package org.moon.core.init.loader;

import org.moon.utils.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * default dictionary loader, load the dictionary definitions from the ~system~dictionary.xml on classpath. the dictionary
 * config support nest dictionary definitions. like :
 * <code>
 *     <dictionary code="sex" name="性别">
 *          <item code="0" name="男"></item>
 *          <item code="1" name="女"></item>
 *      </dictionary>
 * </code>
 *
 * <ol>
 *     <li>dictionary tag declare a new dictionary, <code>code</code> property is the dictionary code, which should not
 *     duplicate. the <code>name</code> property is the description or display text of dictionary</li>
 *     <li>the <code>item</code> tag is the declare the sub dictionary, called dictionary parameter. it also has two properties:
 *     <code>code</code> and <code>name</code>, the means is same as the dictionary tag's properties</li>
 * </ol>
 *
 * @author GavinCook
 * @since 1.0.0
 * @date 2014-12-02
 */
public class DictionaryLoader {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * load menus from ~system~dictionary.xml
     * @return the menus in format:
     * <code>
     *  [
     *      {
     *          name:xxx,
     *          code:xxx,
     *          isFinal:true,
     *          children:[
     *              {
     *                  name:xxx,
     *                  code:xxx,
     *                  isFinal:true
     *              },
     *              ...
     *          ]
     *      },
     *      ...
     *  ]
     * </code>
     *
     * the isFinal property is always <code>true</code>, used difference from the dictionaries added by customer manually
     */
    public List<Map<String,Object>> getDictionaries(){
        List<Map<String, Object>> list = new ArrayList<>();
        try(InputStream inputStream = DictionaryLoader.class.getResourceAsStream("/~system~dictionary.xml")) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            Element element = document.getDocumentElement();
            NodeList dictionaries = element.getChildNodes();

            for (int i = 0; i < dictionaries.getLength(); i++) {
                if (dictionaries.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element dictionaryElement = (Element) dictionaries.item(i);
                    Map<String, Object> dictionary = Maps.mapItSO("isFinal", true, "name", dictionaryElement.getAttribute("name"), "code", dictionaryElement.getAttribute("code"));
                    list.add(dictionary);

                    NodeList childNodes = dictionaryElement.getChildNodes();
                    List<Map<String,Object>> children = new ArrayList<>();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Element subDictionaryElement = (Element) childNodes.item(j);
                            if ("item".equals(childNodes.item(j).getNodeName())) {
                                children.add(Maps.mapItSO("isFinal", true, "name", subDictionaryElement.getAttribute("name"), "code", subDictionaryElement.getAttribute("code")));
                            }
                        }
                    }
                    if (children.size() > 0) {
                        dictionary.put("children", children);
                    }
                }
            }

        }catch (Exception e){
            logger.warn("Failed to load the dictionaries from the ~system~dictionary.xml" , e);
        }
        return list;
    }

}
