package org.moon.rbac.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.moon.base.domain.eventhandler.BaseEventHandler;
import org.moon.rbac.domain.Menu;
import org.moon.rbac.repository.MenuRepository;
import org.moon.rbac.service.MenuService;
import org.springframework.stereotype.Component;

import com.reeham.component.ddd.annotation.OnEvent;

/**
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27 
 */
@Component
public class MenuEventHandler extends BaseEventHandler<Menu>{

	@Resource
	private MenuRepository menuRepository;
	
	@Resource
	private MenuService menuService;
	@OnEvent("getParentMenu")
	public Menu getParent(Menu menu){
		if(menu.getParentId()!=null)
		return menuService.load(menu.getParentId());
		else
			if(menu.getParentCode()!=""){
				return menuService.getMenuByCode(menu.getParentCode());
			}
			else
				return null;
	}
	
	@OnEvent("getTopMenusByRole")
	public List<Menu> getTopMenusByRole(Long rid){
		return menuService.getTopMenusByRole(rid);
	}
	
    @Override
    public Menu save(Menu menu) {
        List<Menu> menus = new ArrayList<Menu>();
        menus.add(menu);
        menuRepository.addMenus(menus);
        return menu;
    }

    @Override
    public void delete(Menu menu) {
        List<Menu> m = new ArrayList<Menu>();
        m.add(menu);
        menuRepository.deleteMenus(m);
    }

    @Override
    public void update(Menu menu) {
        menuRepository.update(menu);
    }
	
}
