package org.moon.rbac.domain.eventhandler;

import com.reeham.component.ddd.annotation.OnEvent;
import org.moon.base.domain.eventhandler.BaseEventHandler;
import org.moon.core.orm.mybatis.DataConverter;
import org.moon.rbac.domain.Menu;
import org.moon.rbac.repository.MenuRepository;
import org.moon.rbac.service.MenuService;
import org.moon.utils.Constants;
import org.moon.utils.Domains;
import org.moon.utils.Dtos;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 菜单事件处理器
 * @author Gavin
 * @date Jul 4, 2014
 */
@Component
public class MenuEventHandler extends BaseEventHandler<Menu, MenuService>{

    private DataConverter<Map> dataConverter = new DataConverter<Map>() {
        @Override
        public Object convert(Map map) {
            return Domains.convertMapToDomain(map,Menu.class);
        }
    };
	@Resource
	private MenuRepository menuRepository;


	@OnEvent("menu/getTopMenusForRole")
	public List<Menu> getTopMenusForRole(Long roleId){

		if(Constants.SYSTEM_ROLEID.equals(roleId)){
            return Dtos.convert(menuRepository.getSubMenus(null), dataConverter);
        }
		return Dtos.convert(menuRepository.getSubMenusByRole(null, roleId), dataConverter);
	}
	
	@OnEvent("menu/getByCode")
	public Menu getByCode(String code){
		return this.service.getMenuByCode(code);
	}
	
	@OnEvent("menu/getSubMenus")
	public List<Menu> getSubMenus(Long parentMenuId){
		return Dtos.convert(menuRepository.getSubMenus(parentMenuId), dataConverter);
	}
	
}
