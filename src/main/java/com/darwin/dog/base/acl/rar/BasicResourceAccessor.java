package com.darwin.dog.base.acl.rar;

import com.darwin.dog.base.acl.AbstractResourceAccessor;
import com.darwin.dog.po.sys.SysUser;
import com.google.common.collect.Sets;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.util.Set;

/**
 * 通用资源访问器
 * 实现 基础 action 控制
 */
public abstract class BasicResourceAccessor extends AbstractResourceAccessor {
    private final ResourceTypeMatch resourceTypeMatch;

    public BasicResourceAccessor(ResourceTypeMatch resourceTypeMatch){
        this.resourceTypeMatch = resourceTypeMatch;
    }

    @Override
    public boolean supportResourceType(String resourceType) {
        return resourceTypeMatch.match(resourceType);
    }

    @Override
    public boolean isAgree(SysUser sysUser, String action, Serializable resource) {
        if (isMultiple(resource)){
            Set<Long> resourceIDs = multipleToSet(resource, Long.class);
            return isAgreeAtResourceIDs(sysUser,action,resourceIDs);
        }
        if (ClassUtils.isAssignableValue(Long.class,resource)){
            return isAgreeAtResourceID(sysUser,action, (Long) resource);
        }
        return false;
    }

    public boolean isAgreeAtResourceID(SysUser sysUser, String action, long resourceID){
       return isAgreeAtResourceIDs(sysUser,action,Sets.newHashSet(resourceID));
    }

    public boolean isAgreeAtResourceIDs(SysUser sysUser, String action, Set<Long> resourceIDs){
        switch (action){
            case ACTION_READ:return read(sysUser,resourceIDs);
            case ACTION_CREATE:return create(sysUser,resourceIDs);
            case ACTION_DELETE:return delete(sysUser,resourceIDs);
            case ACTION_UPDATE:return update(sysUser,resourceIDs);
        }
        return false;
    }

    public abstract boolean read(SysUser sysUser, Set<Long> resourceIDs);
    public abstract boolean create(SysUser sysUser, Set<Long> resourceIDs);
    public abstract boolean delete(SysUser sysUser, Set<Long> resourceIDs);
    public abstract boolean update(SysUser sysUser, Set<Long> resourceIDs);
}
