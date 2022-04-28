package com.darwin.dog.base.acl;

import com.darwin.dog.po.sys.SysUser;
import org.springframework.security.core.Authentication;
import org.springframework.util.ClassUtils;

public abstract class AbstractPermissionChecker implements PermissionChecker {

    protected SysUser getUser(Authentication authentication){
        if(ClassUtils.isAssignableValue(SysUser.class,authentication.getDetails())){
            return (SysUser) authentication.getDetails();
        }
        return null;
    }

}
