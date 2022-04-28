package com.darwin.dog.base.acl;

import org.springframework.security.core.Authentication;

import java.io.Serializable;

/**
 * TOP 接口
 * 权限检查器
 */
public interface PermissionChecker {
    boolean check(Authentication authentication , String resourceType, String action, Serializable resource);
}
