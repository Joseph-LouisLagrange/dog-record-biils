package com.darwin.dog.service.inf.sys;

import com.darwin.dog.po.sys.Permission;
import com.darwin.dog.po.sys.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Transactional
public interface RoleService {
    /**************错误码登记区**************/
    int MISS_ROLE_BY_ID = 1;
    int MISS_ROLE_BY_NAME = 2;
    /***********************/

    /**
     *
     * @param roleID 角色 ID
     * @return 该角色拥有的静态权限
     */
    @PreAuthorize("isAuthenticated()")
    Set<Permission> getPermissionsOfRole(long roleID);

    /**
     *
     * @param roleName 角色名
     * @return 该角色拥有的静态权限
     */
    @PreAuthorize("isAuthenticated()")
    Set<Permission> getPermissionsOfRole(String roleName);

    Role create(Role role);

    Role read(long roleID);

    Role read(String roleName);
}
