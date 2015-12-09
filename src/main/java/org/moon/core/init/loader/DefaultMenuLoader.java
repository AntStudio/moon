package org.moon.core.init.loader;

import org.moon.exception.ApplicationRunTimeException;
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
 * default menu loader, try to load menu definitions from the ~system~menu.xml on classpath. the menu can be nest,like"
 * <code>
 *  <menu code="test" name="测试菜单" iconCss="fa fa-code">
 *      <menu  code="test_1" name="table测试" url="/turn?page=pages/test/table"/>
 *      <menu  code="test_2" name="文件上传" url="/turn?page=pages/test/fileupload"/>
 *  </menu>
 * </code>
 * <ol>
 *     <li>the menu tag declare a new menu, <code>code</code> property stands for menu code, should not duplicate,
 *     <code>name</code> property used to display on page, <code>iconCss</code> is the menu icon style, <code>url</code>
 *     property is the url which should be jump when clicked.
 *     </li>
 *     <li>now only support two levels, the first level called parent menu, which should not has url property, it should
 *     only expand the sub menus when click on page</li>
 *     <li>the second level called sub menus, must definite the url property. In general, the sub menus' code should be in
 *     format <code>{parent menu code}+"_"+{serial number or sub menu name}</code></li>
 * </ol>
 *
 * @author GavinCook
 * @since 1.0.0
 * @date 2012-12-11
 * @see org.moon.core.init.Initializer
 */
public class DefaultMenuLoader {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * load menus from ~system~menu.xml
     * @return the menus in format:
     * <code>
     *  [
     *      {
     *          menuName:xxx,
     *          code:xxx,
     *          iconCss:xxx,
     *          children:[
     *              {
     *                  menuName:xxx,
     *                  code:xxx,
     *                  url:xxx
     *              },
     *              ...
     *          ]
     *      },
     *      ...
     *  ]
     * </code>
     */
    public List<Map<String,Object>> getMenus(){

        List<Map<String, Object>> list = new ArrayList<>();
        try (InputStream inputStream = DefaultMenuLoader.class.getResourceAsStream("/~system~menu.xml")){
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            Element element = document.getDocumentElement();
            NodeList menus = element.getChildNodes();

            for (int i = 0; i < menus.getLength(); i++) {
                if (menus.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element menuElement = (Element) menus.item(i);
                    Map<String, Object> menu = Maps.mapItSO("menuName", menuElement.getAttribute("name"),
                            "code", menuElement.getAttribute("code"), "iconCss", menuElement.getAttribute("iconCss"));
                    list.add(menu);

                    NodeList childNodes = menuElement.getChildNodes();
                    List<Map<String, Object>> children = new ArrayList<>();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Element subMenuElement = (Element) childNodes.item(j);
                            if ("menu".equals(childNodes.item(j).getNodeName())) {
                                children.add(Maps.mapItSO("url", subMenuElement.getAttribute("url"), "menuName", subMenuElement.getAttribute("name"), "code", subMenuElement.getAttribute("code")));
                            }
                        }
                    }
                    if (children.size() > 0) {
                        menu.put("children", children);
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to load the menus from the ~system~menu.xml" , e);
        }
        return list;
    }
}
