package org.moon.rbac.service.impl;

import com.reeham.component.ddd.model.ModelContainer;
import org.moon.base.service.AbstractService;
import org.moon.core.orm.mybatis.Criteria;
import org.moon.core.orm.mybatis.DataConverter;
import org.moon.core.orm.mybatis.criterion.Restrictions;
import org.moon.rbac.domain.Menu;
import org.moon.rbac.domain.Role;
import org.moon.rbac.repository.MenuRepository;
import org.moon.rbac.service.MenuService;
import org.moon.utils.Constants;
import org.moon.utils.Dtos;
import org.moon.utils.Maps;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class MenuServiceImpl extends AbstractService<Menu> implements MenuService {
	@Resource
	private MenuRepository menuRepository;
	
	@Resource
	private ModelContainer modelContainer;

	@Override
	public List<Menu> getSubMenusForRole(Long parentId, Long rid) {
		if(Constants.SYSTEM_ROLEID.equals(rid)){
			return modelContainer.identifiersToModels((List)menuRepository.getSubMenu(parentId), Menu.class, this);
		}
		if(rid==null||rid<=0){
			return new ArrayList<Menu>();
		}
		return modelContainer.identifiersToModels((List)menuRepository.getSubMenuByRole(parentId, rid), Menu.class, this);
	}

	@Override
	public List getMenusWithStatus(Long parentMenuId, Long rid) {
		final Role role = loadDomain(Role.class, rid);
		DataConverter<Menu> converter = new DataConverter<Menu>() {
			@Override
			public Object convert(Menu m) {
				return Maps.mapIt("id"       , m.getId(),
								  "menuName" , m.getMenuName(), 
								  "code"     , m.getCode(), 
								  "checked"  , role.hasMenu(m.getCode()),
								  "url"      , m.getUrl());
			}
		};
		
		List<Menu> menus;
		if(parentMenuId==null||parentMenuId<0){
			menus = getTopMenus();
		}else{
			menus = get(parentMenuId).getSubMenus();
		}
		return Dtos.convert(menus, converter);
	}
	
	
	@Override
	public List<Menu> getTopMenus() {
		return getSubMenus(null);
	}
	

	private List<Menu> getSubMenus(Long parentId){
		return modelContainer.identifiersToModels((List)menuRepository.getSubMenu(parentId), Menu.class, this);
	}
	
	@Override
	public void assignMenuToRole(Long[] menuIds,Boolean[] checkStatus, Long rid) {
		List<Menu> addMenus = new ArrayList<Menu>();
		List<Menu> removeMenus= new ArrayList<Menu>();
		for(int i=0,j=menuIds.length;i<j;i++){
            if (menuIds[i] == -1) {// 前台根节点不处理
                continue;
            }
			if(true==checkStatus[i]){
				Menu menu = get(menuIds[i]);
				if(menu.getParentCode()!=null){
					menu.setParentCode(menu.getParent().getCode());
				}
				addMenus.add(menu);
			}
			else{
				removeMenus.add(get(menuIds[i]));
			}
		}
        if (addMenus.size() > 0) {
            addMenusToRole(addMenus, rid);
        }
        if (removeMenus.size() > 0) {
            removeMenuToRole(removeMenus, rid);
        }
	}


	private void addMenusToRole(List<Menu> menus, Long rid) {
		menuRepository.addMenusToRole(menus,rid);
	}

	private void removeMenuToRole(List<Menu> menus, Long rid) {
		menuRepository.removeMenusFromRole(menus,rid);
	}
		
	@Override
	public Menu getMenuByCode(String code) {
		if (code == null) {
			return null;
		} else {
			return this.get(menuRepository.getByCode(code));
		}
	}

	@Override
	public void sortMenus(Long parentId, Long[] childrenIds) {
		int order = 0;
		for(Long id:childrenIds){
			Menu menu = get(id);
			menu.setMenuOrder(order++);
		}
		if(parentId==-1L){
			menuRepository.sortMenu(null, childrenIds,null);
		}else{
			menuRepository.sortMenu(parentId, childrenIds,get(parentId).getCode());
		}
		
	}
	
	@Override
	public Map<String, Menu> getSystemMenusByCode() {
		Criteria criteria = new Criteria();
		criteria.add(Restrictions.isNull("parent_id")).add(Restrictions.notNull("code"));
		Map<String,Menu> m = new HashMap<String, Menu>();
		List<Menu> menus =listForDomain(criteria);
		for(Menu menu:menus){
			m.put(menu.getCode(), menu);
		}
		return m;
	}
	
	@Override
	public void addMenus(List<Menu> menus) {
		menuRepository.addMenus(menus);
	}
}
