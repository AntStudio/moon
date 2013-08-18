package org.antstudio.rbac.domain.init.loader;

import java.util.ArrayList;
import java.util.List;

import org.antstudio.rbac.domain.Menu;
import org.apache.log4j.Logger;


/**
 * 
 * @author Gavin
 * @version 1.0
 * @date 2012-12-11
 */
public class DefaultMenuLoader implements MenuLoader{
	private Logger log = Logger.getLogger(getClass());
	@Override
	public List<Menu> getMenus() {
		List<Menu> menus = new ArrayList<Menu>();
		menus.add(new Menu("平台管理","","100000",""));
		log.info("[spring-rbac] get menu:[平台管理]");
		return menus;
	}


}
