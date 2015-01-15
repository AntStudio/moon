package org.moon.core.init;

import org.moon.core.init.loader.DefaultMenuLoader;
import org.moon.core.init.loader.DictionaryLoader;
import org.moon.core.orm.mybatis.Criteria;
import org.moon.core.orm.mybatis.criterion.Restrictions;
import org.moon.dictionary.service.DictionaryService;
import org.moon.rbac.domain.Menu;
import org.moon.rbac.domain.Permission;
import org.moon.core.init.helper.MenuMappingHelper;
import org.moon.core.init.helper.PermissionMappingHelper;
import org.moon.rbac.service.MenuService;
import org.moon.rbac.service.PermissionService;
import org.moon.utils.Constants;
import org.moon.utils.Maps;
import org.moon.utils.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Initializer implements ApplicationListener<ContextRefreshedEvent> {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	@Resource
	private MenuService menuService;

	@Resource
	private PermissionService permissionService;

    @Resource
    private DictionaryService dictionaryService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event instanceof ContextRefreshedEvent) {
            loadMenu();

			log.info("[spring-rbac]Success init the permissions");
			
			log.info("[spring-rbac]Starting init the permissions");
			Map<String, Permission> permissions = permissionService.getPermissionsByCode();
			if (permissions.keySet().size() == 0) {//当系统中没有菜单时，直接保存
				permissionService.batchSave(PermissionMappingHelper
						.getMappedPermissions());
			} else {
				List<Permission> addPermissions = new ArrayList<Permission>(),  updatePermissions = new ArrayList<Permission>();
				List<Long> deletePermissions = new ArrayList<Long>();
				Map<String, Permission> mappedPermissions = PermissionMappingHelper
						.getPermissionsMapByCode();
				for (String code : mappedPermissions.keySet()) {
					if (permissions.containsKey(code)) {
						updatePermissions
								.add(getUpdatePermission(
										mappedPermissions.get(code),
										permissions.get(code)));
						permissions.remove(code);
					} else {
                        addPermissions.add(mappedPermissions.get(code));
                    }
				}
				for (Permission p : permissions.values()) {
					deletePermissions.add(p.getId());
				}
				if (addPermissions.size() != 0)
					permissionService.batchSave(addPermissions);
				if (deletePermissions.size() != 0)
					permissionService.delete(deletePermissions.toArray(new Long[]{}),false);
				for (Permission p : updatePermissions) {
					p.update();
				}
			}

            log.info("[spring-rbac]Starting init the dictionaries");
            DictionaryLoader dictionaryLoader = new DictionaryLoader();
            List<Map<String,Object>> dictionaries =  dictionaryLoader.getDictionaries();

            //查询出系统字典
            Criteria criteria = new Criteria();
            criteria.add(Restrictions.eq("isFinal",true));
            List<Map> systemDictionaries = dictionaryService.list(criteria);//系统的字典

            Map<String,Map> codes = new HashMap<String, Map>();//将系统字典按照code排列，用于后面判断字典是否存在
            Object parentId;
            for(Map m:systemDictionaries){
                parentId = m.get("parentId");
                codes.put((Objects.nonNull(parentId)?parentId:"")+"$_$"+(String)m.get("code"),m);
            }

            //处理字典，处理逻辑为：如果字典代码存在，则不处理，否则新增
            Object dictionaryId;
            for(Map<String,Object> dictionary:dictionaries){

                if(!codes.containsKey("$_$"+dictionary.get("code"))){
                    dictionaryService.add(dictionary);//处理字典项
                    dictionaryId = dictionary.get("id");
                }else{
                    dictionaryId = codes.get("$_$"+dictionary.get("code")).get("id");
                }


                if(dictionary.containsKey("children")){//如果有字典值
                    for(Map<String,Object> item:(List<Map<String,Object>>)dictionary.get("children")){
                        if(!codes.containsKey(dictionaryId+"$_$"+item.get("code"))){
                            item.put("parentId", dictionaryId);
                            dictionaryService.add(item);
                        }
                    }
                }
            }
		}

	}

    /**
     * 加载菜单
     */
    private void loadMenu(){
        //注意：顶级菜单只能通过配置文件配置，注解上的菜单一律为子菜单！

        log.info("[spring-rbac] Starting init menu");
        //加载出数据库的系统菜单
        List<Map> systemMenus = menuService.getSystemMenus();

        //配置文件中的菜单
        DefaultMenuLoader defaultMenuLoader = new DefaultMenuLoader();
        List<Map<String,Object>> menus = defaultMenuLoader.getMenus();

        //注解上的菜单
        Map<String,List<Menu>> menusOnAnnotation = MenuMappingHelper.getMenusByParentCode();

        //把系统菜单按照code->menu的方式组织，对于顶级菜单为"$_$"+code,对于子菜单用 父菜单ID+"$_$"+code
        Map<String,Map> codes = new HashMap<String, Map>();
        for(Map m:systemMenus){
            codes.put((Objects.nonNull(m.get("parentId")) ? m.get("parentId") : "") + "$_$" + m.get("code"),m);
        }

        //菜单ID，用于存储父菜单id,在处理子菜单时使用
        Object menuId;
        for(Map<String,Object> menu:menus){
            Object code = menu.get("code");
            if(codes.containsKey("$_$"+code)){//如果有对应的菜单
                menuId = codes.get("$_$"+code).get("id");
            }else{
                menuService.add(menu);//新增菜单
                menuId = menu.get("id");
            }

            //处理配置文件中的子菜单
            if(menu.containsKey("children")){
                for(Map<String,Object> item:(List<Map<String,Object>>)menu.get("children")){
                    if(!codes.containsKey(menuId+"$_$"+item.get("code"))){
                        item.put("parentId", menuId);
                        menuService.add(item);
                    }
                }
            }

            //处理注解菜单
            if(Objects.nonNull(menusOnAnnotation.get(code))){
                for(Menu menuOnAnnotation:menusOnAnnotation.get(code)){
                    if(!codes.containsKey(menuId+"$_$"+menuOnAnnotation.getCode())){
                        menuService.add(Maps.mapIt("menuName",menuOnAnnotation.getMenuName(),
                                "url",menuOnAnnotation.getUrl(),
                                "parentCode",menuOnAnnotation.getParentCode(),
                                "code",menuOnAnnotation.getCode(),
                                "parentId",menuId));
                    }
                }
            }
        }
    }

	private Permission getUpdatePermission(Permission mappingPermission,Permission permission) {
		permission.setName(mappingPermission.getName());
		permission.setCode(mappingPermission.getCode());
		return permission;
	}

}
