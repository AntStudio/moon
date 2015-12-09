package org.moon.core.auth;

import org.moon.core.session.SessionContext;
import org.moon.rbac.domain.Role;
import org.moon.rbac.domain.User;
import org.moon.rbac.service.UserService;
import org.moon.utils.Objects;

import javax.annotation.Resource;

/**
 * the default implementation for {@link AuthorizationChecker}
 * @author GavinCook
 * @since 1.0.0
 */
public class DefaultAuthorizationChecker implements AuthorizationChecker {

    @Resource
    private UserService userService;

    @Override
    public boolean canAccess(String permissionCode) {
        boolean canAccess;

        Long currentUserId = (Long) SessionContext.getSession().getAttribute(User.CURRENT_USER_ID);
        Role currentRole = null;
        User currentUser ;
        if (currentUserId != null) {
            currentUser = userService.get(currentUserId);
            currentRole = currentUser.getRole();
        }
        canAccess = currentRole != null && currentRole.hasPermission(permissionCode);

        return canAccess;
    }

    @Override
    public boolean check(Role role, String permissionCode) {
        return !(Objects.isNull(role) || Objects.isNull(permissionCode)) && role.hasPermission(permissionCode);
    }
}
