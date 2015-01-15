package org.moon.core.spring;

import org.moon.rbac.domain.Menu;
import org.moon.core.init.helper.MenuMappingHelper;
import org.moon.core.init.loader.MenuLoader;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;


/**
 * 自定义spring类扫描filter处理，主要处理菜单和权限的加载.
 * @author Gavin
 * @version 1.0
 * @date 2012-12-12
 */
public class DomainTypeFilter implements TypeFilter {
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean match(MetadataReader arg0, MetadataReaderFactory arg1)
			throws IOException {
		ClassMetadata metadata = arg0.getClassMetadata();
		boolean menuProvider = false;//判断是否是菜单提供器(即是否实现MenuLoader接口)
		for(String name:metadata.getInterfaceNames()){
			if(name.equals(MenuLoader.class.getName())){
				menuProvider = true;
				break;
			}
		}
		if(menuProvider){//匹配MenuLoader接口，菜单初始化器必须实现MenuLoader接口，并复写getMenus方法
			try {
				Class<?> loader = Class.forName(arg0.getClassMetadata().getClassName());
				for(Constructor<?> con:loader.getConstructors()){
					if(con.getParameterTypes().length==0){
						Object o = con.newInstance();
						Method method = loader.getMethod("getMenus");
						Object list =  method.invoke(o);
						if(list==null)
							throw new Exception("The result of getMenus can't be null in the class:"+loader.getName());
						List<Menu> menuList = (List<Menu>) list;
						for(Menu m:menuList){
							if("".equals(m.getParentCode()))
								m.setParentCode(null);
							MenuMappingHelper.addMappingMenu(m);
						}
						
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}

}
