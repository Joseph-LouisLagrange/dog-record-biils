package com.darwin.dog.service.impl.sys;

import com.darwin.dog.po.sys.Permission;
import com.darwin.dog.repository.sys.PermissionRepository;
import com.darwin.dog.service.inf.sys.PermissionService;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public Permission create(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Set<Permission> readAll() {
        return Sets.newHashSet(permissionRepository.findAll());
    }
}
