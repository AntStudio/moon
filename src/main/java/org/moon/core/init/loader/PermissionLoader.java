package org.moon.core.init.loader;

import org.moon.rbac.domain.Permission;

import java.util.List;

/**
 * permission loader
 */
public interface PermissionLoader {

    List<Permission> getPermissions();

}
