package org.moon.rbac.domain.eventhandler;

import com.reeham.component.ddd.annotation.OnEvent;
import org.moon.base.domain.eventhandler.BaseEventHandler;
import org.moon.core.orm.mybatis.DataConverter;
import org.moon.rbac.domain.Permission;
import org.moon.rbac.repository.PermissionRepository;
import org.moon.rbac.service.PermissionService;
import org.moon.utils.Domains;
import org.moon.utils.Dtos;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Gavin
 * @date Jul 9, 2014
 */
@Component
public class PermissionEventHandler extends BaseEventHandler<Permission, PermissionService> {

    private DataConverter<Map> dataConverter = new DataConverter<Map>() {
        @Override
        public Object convert(Map map) {
            return Domains.convertMapToDomain(map, Permission.class);
        }
    };

    @Resource
    private PermissionRepository permissionRepository;

    @OnEvent("permission/getPermissionForRole")
    public List<Permission> getPermissionForRole(Long rid) {
        return Dtos.convert(permissionRepository.getPermissionsByRole(rid),dataConverter);
    }

}
