package org.moon.core.init.loader;

import org.moon.rbac.domain.Permission;
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

/**
 * default permission loader, try to load menu definitions from the ~system~permission.xml on classpath.
 * the permission tag declare a new permission, it has two properties, <code>code</code> is the unique permission code,
 * and the <code>name</code> property is the description for permission. current, not support the permission nest.
 *
 * @author GavinCook
 * @since 1.0.0
 * @date 2015-07-23
 * @see org.moon.core.init.Initializer
 */
public class DefaultPermissionLoader implements PermissionLoader {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Permission> getPermissions() {
        {
            List<Permission> list = new ArrayList<>();
            try(InputStream inputStream = DictionaryLoader.class.getResourceAsStream("/~system~permission.xml")) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(inputStream);
                Element element = document.getDocumentElement();
                NodeList permissions = element.getChildNodes();

                for (int i = 0, l = permissions.getLength(); i < l; i++) {
                    if (permissions.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element dictionaryElement = (Element) permissions.item(i);
                        Permission permission = new Permission();
                        permission.setName(dictionaryElement.getAttribute("name"));
                        permission.setCode(dictionaryElement.getAttribute("code"));
                        list.add(permission);
                    }
                }

            }catch (Exception e){
                logger.warn("Failed to load the permissions from the ~system~permission.xml" , e);
            }
            return list;
        }
    }
}
