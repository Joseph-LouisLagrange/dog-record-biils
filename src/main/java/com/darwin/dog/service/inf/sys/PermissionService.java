package com.darwin.dog.service.inf.sys;

import com.darwin.dog.po.sys.Permission;

import java.util.Set;

public interface PermissionService {
    Permission create(Permission permission);
    Set<Permission> readAll();
}
