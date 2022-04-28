package com.darwin.dog.base.acl.rar;

import com.darwin.dog.base.acl.ResourceAccessor;
import com.darwin.dog.po.sys.SysUser;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component("denyResourceAccessor")
public class DenyResourceAccessor implements ResourceAccessor {
    @Override
    public boolean supportResourceType(String resourceType) {
        return false;
    }

    @Override
    public boolean isAgree(SysUser sysUser, String action, Serializable resource) {
        return false;
    }
}
