package org.moon.core.auth;

import org.moon.rbac.domain.Role;

/**
 * authorization checker, Programmatic apis for authorization.
 * @author GavinCook
 * @since 1.0.0
 * @see DefaultAuthorizationChecker
 * @see org.moon.rbac.domain.annotation.PermissionMapping
 * @see org.moon.rbac.interceptor.RbacInterceptor
 */
public interface AuthorizationChecker {

    /**
     * check current login role can access the permission with code <code>permissionCode</code> or not
     * @param permissionCode the permission code
     * @return <code>true</code> if can access, OR return <code>false</code>
     */
    boolean canAccess(String permissionCode);

    /**
     * check the role can access the permission with code <code>permissionCode</code> or not
     * @param role the role need check
     * @param permissionCode the permission code
     * @return <code>true</code> if can access, OR return <code>false</code>
     */
    boolean check(Role role, String permissionCode);

}
