package org.moon.rbac.domain.eventhandler;

import com.reeham.component.ddd.annotation.OnEvent;
import com.reeham.component.ddd.model.ModelContainer;
import org.moon.base.domain.eventhandler.BaseEventHandler;
import org.moon.rbac.domain.Menu;
import org.moon.rbac.repository.MenuRepository;
import org.moon.rbac.service.MenuService;
import org.moon.utils.Constants;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单事件处理器
 * @author Gavin
 * @date Jul 4, 2014
 */
@Component
public class MenuEventHandler extends BaseEventHandler<Menu, MenuService>{
	
	@Resource
	private MenuRepository menuRepository;
	
	@Resource
	private ModelContainer modelContainer;

	@OnEvent("menu/getTopMenusForRole")
	public List<Menu> getTopMenusForRole(Long roleId){
		if(Constants.SYSTEM_ROLEID.equals(roleId)){
			return modelContainer.identifiersToModels((List)menuRepository.getSubMenu(null), Menu.class, this.service);
		}
		return modelContainer.identifiersToModels((List)menuRepository.getSubMenuByRole(null, roleId), Menu.class, this.service);
	}
	
	@OnEvent("menu/getByCode")
	public Menu getByCode(String code){
		return this.service.getMenuByCode(code);
	}
	
	@OnEvent("menu/getSubMenus")
	public List<Menu> getSubMenus(Long parentMenuId){
		return modelContainer.identifiersToModels((List)menuRepository.getSubMenu(parentMenuId), Menu.class, this.service);
	}
	
}
