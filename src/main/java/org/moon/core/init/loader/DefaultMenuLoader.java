package org.moon.core.init.loader;

import org.apache.log4j.Logger;
import org.moon.rbac.domain.Menu;
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
 * @author Gavin
 * @version 1.0
 * @date 2012-12-11
 */
public class DefaultMenuLoader {
    /**
     * 菜单加载器,从~system~menu.xml中加载
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getMenus(){
        InputStream inputStream = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            inputStream = DefaultMenuLoader.class.getResourceAsStream("/~system~menu.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            Element element = document.getDocumentElement();
            NodeList menus = element.getChildNodes();

            for (int i = 0; i < menus.getLength(); i++) {
                if (menus.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element menuElement = (Element) menus.item(i);
                    Map<String, Object> menu = Maps.mapIt("menuName", menuElement.getAttribute("name"), "code", menuElement.getAttribute("code"));
                    list.add(menu);

                    NodeList childNodes = menuElement.getChildNodes();
                    List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Element subMenuElement = (Element) childNodes.item(j);
                            if ("menu".equals(childNodes.item(j).getNodeName())) {
                                children.add(Maps.mapIt("url", subMenuElement.getAttribute("url"), "menuName", subMenuElement.getAttribute("name"), "code", subMenuElement.getAttribute("code")));
                            }
                        }
                    }
                    if (children.size() > 0) {
                        menu.put("children", children);
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
