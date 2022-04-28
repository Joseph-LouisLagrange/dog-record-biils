package com.darwin.dog.base.acl;

import com.darwin.dog.po.sys.SysUser;
import com.darwin.dog.po.sys.Permission;
import com.darwin.dog.po.sys.Role;
import com.darwin.dog.service.inf.sys.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;



@Component("staticPermissionChecker")
public class StaticPermissionChecker extends AbstractPermissionChecker {

    @Autowired
    RoleService roleService;

    public Set<Permission> getPermissions(Authentication authentication){
        SysUser sysUser = getUser(authentication);
        if (sysUser ==null){
            return authentication.getAuthorities().stream()
                    .parallel()
                    .map(GrantedAuthority::getAuthority)
                    .map(roleName -> roleService.getPermissionsOfRole(roleName))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        }
        return sysUser.getRoles().stream()
                .map(Role::getPermissions)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }


    @Override
    public boolean check(Authentication authentication, String resourceType, String action, Serializable resource) {
        Set<Permission> permissions = getPermissions(authentication);
        return permissions.stream()
                .parallel()
                .anyMatch(p-> p.isAgree(resourceType,action,(String) resource));
    }
}
