package org.moon.rbac.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.moon.rbac.domain.Menu;
import org.moon.rbac.repository.MenuRepository;
import org.moon.rbac.service.MenuService;
import org.moon.utils.Constants;
import org.springframework.stereotype.Service;

import com.reeham.component.ddd.annotation.OnEvent;
import com.reeham.component.ddd.model.ModelContainer;
import com.reeham.component.ddd.model.ModelUtils;
@Service
public class MenuServiceImpl implements MenuService {
	@Resource
	MenuRepository menuRepository;
	
	@Resource
	public ModelContainer modelContainer;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Menu> getSubMenusByRole(Long parentId, Long rid) {
		if(rid==null||rid<=0)
			return new ArrayList<Menu>();
		return modelContainer.identifiersToModels((List)menuRepository.getSubMenuByRole(parentId, rid), Menu.class, this);
	}

	@Override
	public Menu load(Long id) {
		
		return menuRepository.get(id);
	}

	@Override
	@OnEvent("menu/get")
	public Menu get(Long id) {
		return (Menu) modelContainer.getModel(ModelUtils.asModelKey(Menu.class, id),this);
	}

	@Override
	public Object loadModel(Object identifier) {
		return load((Long)identifier);
	}

	@Override
    public List<Menu> getTopMenusByRole(Long rid) {
        if (Constants.SYSTEM_ROLEID.equals(rid)) {
            return getSubMenus(null);
        }
        if (rid == null || rid <= 0) {
            return new ArrayList<Menu>();
        }
        return getSubMenusByRole(null, rid);
    }

	@Override
	public List<Map<String,Object>> getSubMenusByRoleForMap(Long parentId, Long rid) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(Constants.SYSTEM_ROLEID.equals(rid))
		for(Menu m:getSubMenus(parentId)){
			list.add(addProperty(m.toMap(),"edit",true));
		}
        else {
            for (Menu m : getSubMenusByRole(parentId, rid)) {
                if (m.isSystemMenu()) {
                    list.add(addProperty(m.toMap(), "edit", false));
                } else {
                    list.add(addProperty(m.toMap(), "edit", true));
                }
            }
        }
		return list;
	}
	
	@Override
	public List<Map<String,Object>> getSubMenusForMap(Long parentId) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Menu m:getSubMenus(parentId)){
			list.add(m.toMap());
		}
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Menu> getSubMenus(Long parentId) {
		return modelContainer.identifiersToModels((List)menuRepository.getSubMenu(parentId), Menu.class, this);
	}

	@Override
	public List<Map<String, Object>> getAssignMenuData(Long pid, Long rid) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Menu> roleMenus = getSubMenusByRole(pid, rid);
        for (Menu m : getSubMenus(pid)) {
            if (roleMenus.contains(m)) {
                list.add(addProperty(m.toMap(), "checked", true));
            } else {
                list.add(addProperty(m.toMap(), "checked", false));
            }
        }
		return list;
	}
 
	
	private Map<String,Object> addProperty(Map<String,Object> m,String key,Object value){
		m.put(key, value);
		return  m;
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
				if(menu.getParentId()==null&&menu.getParentCode()!=null){
					menu.setParentId(menu.getParent().getId());
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

	@Override
	public void addMenusToRole(List<Menu> menus, Long rid) {
		menuRepository.addMenusToRole(menus,rid);
	}

	@Override
	public void removeMenuToRole(List<Menu> menus, Long rid) {
		menuRepository.removeMenusFromRole(menus,rid);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Menu> getAllMenus(Boolean system) {
		return modelContainer.identifiersToModels((List)menuRepository.getAllMenus(system, false), Menu.class, this);
	}

	@Override
	public void addMenus(List<Menu> menus) {
		menuRepository.addMenus(menus);
		
	}

	@Override
	public void deleteMenus(List<Menu> menus) {
		menuRepository.deleteMenus(menus);
		
	}

	@Override
	public Map<String, Menu> getSystemMenusByCode() {
		Map<String,Menu> menusMap = new HashMap<String,Menu>();
		Menu menu;
		for(Long id:menuRepository.getAllMenus(true, false)){
			menu = get(id);
			menusMap.put(menu.getCode(), menu);
		}
		return menusMap;
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
}
