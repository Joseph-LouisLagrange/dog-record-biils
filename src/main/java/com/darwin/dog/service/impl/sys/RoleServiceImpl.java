package com.darwin.dog.service.impl.sys;

import com.darwin.dog.po.sys.Permission;
import com.darwin.dog.po.sys.Role;
import com.darwin.dog.exception.BaseExceptionType;
import com.darwin.dog.exception.CommonException;
import com.darwin.dog.repository.sys.RoleRepository;
import com.darwin.dog.service.inf.sys.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Set<Permission> getPermissionsOfRole(long roleID) {
           return read(roleID).getPermissions();
    }

    @Override
    public Set<Permission> getPermissionsOfRole(String roleName) {
              return read(roleName).getPermissions();
    }

    @Override
    public Role create(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role read(long roleID) {
        return roleRepository.findById(roleID)
                .orElseThrow(()->CommonException.of(BaseExceptionType.MISS,MISS_ROLE_BY_ID,"角色 ID 未命中"));
    }

    @Override
    public Role read(String roleName) {
        return roleRepository.findRoleByName(roleName)
                .orElseThrow(() -> CommonException.of(BaseExceptionType.MISS, MISS_ROLE_BY_NAME, "角色名未命中"));
    }
}
