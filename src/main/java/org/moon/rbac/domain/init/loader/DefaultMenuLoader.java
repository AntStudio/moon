package org.moon.rbac.domain.init.loader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.moon.rbac.domain.Menu;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.DefaultHandler;


/**
 * @author Gavin
 * @version 1.0
 * @date 2012-12-11
 */
public class DefaultMenuLoader implements MenuLoader{
	private Logger log = Logger.getLogger(getClass());
	@Override
	public List<Menu> getMenus() {
		List<Menu> menus = new ArrayList<Menu>();
		menus.add(new Menu("平台管理","","platform",""));
		log.info("[spring-rbac] get menu:[平台管理]");
		try {
			menus.addAll(new DomParseService().getMenus(DefaultMenuLoader.class.getResourceAsStream("/~system~menu.xml")));
		} catch (Exception e) {
			log.info("[spring-rbac] DefaultMenuLoader:加载菜单出错(~system~menu.xml).");
			e.printStackTrace();
		}
		return menus;
	}

	class DomParseService extends DefaultHandler{
		public DomParseService(){
			
		}
		public List<Menu> getMenus(InputStream inputStream) throws Exception{ 
		        List<Menu> list = new ArrayList<Menu>(); 
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
		        DocumentBuilder builder = factory.newDocumentBuilder(); 
		        Document document = builder.parse(inputStream);
		        Element element = document.getDocumentElement(); 
		        NodeList menus = element.getChildNodes();
		        for(int i=0;i<menus.getLength();i++){ 
		        	if(menus.item(i).getNodeType()==Node.ELEMENT_NODE){
		            Element menuElement = (Element) menus.item(i); 
		            Menu menu = new Menu(menuElement.getAttribute("name"),"",menuElement.getAttribute("code"),null);
		            list.add(menu); 
		            NodeList childNodes = menuElement.getChildNodes(); 
		            for(int j=0;j<childNodes.getLength();j++){ 
		            	 
		            	if(childNodes.item(j).getNodeType()==Node.ELEMENT_NODE){
		            	 Element subMenuElement = (Element) childNodes.item(j); 
		                    if("menu".equals(childNodes.item(j).getNodeName())){ 
		                    	 list.add(new Menu(subMenuElement.getAttribute("name"),subMenuElement.getAttribute("url"),subMenuElement.getAttribute("code"),menuElement.getAttribute("code")));  
		                    }
		            	}
		            }//end for j 
		        	}
		        }//end for i 
 		        return list; 
		    } 
	}
}
