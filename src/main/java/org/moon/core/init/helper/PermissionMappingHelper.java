package org.moon.core.init.helper;

import org.moon.rbac.domain.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * the helper used to hold all the permission definitions for what annotated {@link org.moon.rbac.domain.annotation.PermissionMapping}
 * in controller and the ones config in ~system~permission.xml file when project starting,
 * then when the spring application context is ready, will flush them into the database.
 *
 * @author GavinCook
 * @since 1.0.0
 * @date 2012-12-14
 * @see org.moon.core.init.Initializer
 */
public class PermissionMappingHelper {

    /**
     * the origin permissions from the pemission mapping annotations
     */
    private static List<Permission> mappedPermission = new ArrayList<>();

    /**
     * the permissions which grouped by the permission code
     */
    private static Map<String, Permission> permissionsMapByCode = new HashMap<>();

    public static void addMappingPermission(Permission permission) {
        mappedPermission.add(permission);
    }

    public static void addAllMappingPermissions(List<Permission> permissions) {
        mappedPermission.addAll(permissions);
    }

    public static Map<String, Permission> getPermissionsMapByCode() {
        if(permissionsMapByCode.isEmpty()) {
            Map<String, List<Permission>> groupedPermissions = mappedPermission.stream().collect(Collectors.groupingBy(Permission::getCode));
            groupedPermissions.entrySet().forEach(entry -> {
                String pointcut = entry.getValue().stream().map(Permission::getPointcut).collect(Collectors.joining(" , "));
                Permission p = new Permission(entry.getKey(), entry.getValue().get(0).getName(), pointcut);
                permissionsMapByCode.put(entry.getKey(), p);
            });
        }
        return permissionsMapByCode;
    }

}
