package org.moon.rbac.service.impl;

import org.moon.base.service.AbstractDomainService;
import org.moon.core.orm.mybatis.Criteria;
import org.moon.core.orm.mybatis.criterion.Restrictions;
import org.moon.rbac.domain.Menu;
import org.moon.rbac.repository.MenuRepository;
import org.moon.rbac.service.MenuService;
import org.moon.utils.Constants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class MenuServiceImpl extends AbstractDomainService<Menu> implements MenuService {
	@Resource
	private MenuRepository menuRepository;

	@Override
	public List<Map> getSubMenusForRole(Long parentId, Long rid) {
		if(Constants.SYSTEM_ROLEID.equals(rid)){
			return menuRepository.getSubMenus(parentId);
		}
		if(rid==null||rid<=0){
			return Collections.emptyList();
		}
		return menuRepository.getSubMenusByRole(parentId, rid);
	}

	@Override
	public List getMenusWithStatus(Long parentMenuId, Long rid) {
        return menuRepository.getMenusWithStatus(parentMenuId,rid);
	}
	
	
	@Override
	public List<Map> getTopMenus() {
		return getSubMenus(null);
	}
	

	private List<Map> getSubMenus(Long parentId){
		return menuRepository.getSubMenus(parentId);
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
            removeMenuFromRole(removeMenus, rid);
        }
	}


	private void addMenusToRole(List<Menu> menus, Long rid) {
		menuRepository.addMenusToRole(menus,rid);
	}

	private void removeMenuFromRole(List<Menu> menus, Long rid) {
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
	public List<Map> getSystemMenus() {
		Criteria criteria = new Criteria();
		criteria.add(Restrictions.eq("isFinal", true));
		return list(criteria);
	}
	
	@Override
	public void addMenus(List<Menu> menus) {
		menuRepository.addMenus(menus);
	}

    @Override
    public void add(Map<String, Object> params) {
        menuRepository.add(params);
    }
}
