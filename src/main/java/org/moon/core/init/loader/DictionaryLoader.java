package org.moon.core.init.loader;

import org.moon.dictionary.domain.Dictionary;
import org.moon.rbac.domain.Menu;
import org.moon.utils.FileUtils;
import org.moon.utils.Maps;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 字典加载器
 * @author:Gavin
 * @date 2014/12/2 0002
 */
public class DictionaryLoader {

    public List<Map<String,Object>> getDictionaries(){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        InputStream inputStream = null;
        try {
            inputStream = DictionaryLoader.class.getResourceAsStream("/~system~dictionary.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            Element element = document.getDocumentElement();
            NodeList dictionaries = element.getChildNodes();

            for (int i = 0; i < dictionaries.getLength(); i++) {
                if (dictionaries.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element dictionaryElement = (Element) dictionaries.item(i);
                    Map<String, Object> dictionary = Maps.mapIt("isFinal",true,"name", dictionaryElement.getAttribute("name"), "code", dictionaryElement.getAttribute("code"));
                    list.add(dictionary);

                    NodeList childNodes = dictionaryElement.getChildNodes();
                    List<Map<String,Object>> children = new ArrayList<Map<String,Object>>();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Element subDictionaryElement = (Element) childNodes.item(j);
                            if ("item".equals(childNodes.item(j).getNodeName())) {
                                children.add(Maps.mapIt("isFinal",true,"name", subDictionaryElement.getAttribute("name"), "code", subDictionaryElement.getAttribute("code")));
                            }
                        }
                    }
                    if (children.size() > 0) {
                        dictionary.put("children", children);
                    }
                }
            }

        }catch (Exception e){
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return list;
    }

}
