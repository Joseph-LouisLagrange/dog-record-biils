package com.darwin.dog.base.acl;

import com.darwin.dog.po.sys.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;


/**
 * 动态权限校验器</br>
 * 主要用于对特定资源的部分进行操作控制</br>
 * 如：Alice 只能操作属于自己的书本或者一堆笔</br>
 * 实现：通过权限校验分发实现，该类仅作为 Caller ，不直接参与校验
 */
@Slf4j
@Component("dynamicPermissionChecker")
public class DynamicPermissionChecker extends AbstractPermissionChecker {


    @Resource(name = "denyResourceAccessor",type = ResourceAccessor.class)
    ResourceAccessor denyResourceAccessor;

    @Autowired
    List<ResourceAccessor> resourceAccessors;

    @Override
    public boolean check(Authentication authentication, String resourceType, String action, Serializable resource) {
        SysUser sysUser = getUser(authentication);
        if (sysUser == null){
            return false;
        }
        return findResourceAccessor(resourceType).isAgree(sysUser,action,resource);
    }

    private ResourceAccessor findResourceAccessor(String resourceType){
        Optional<ResourceAccessor> accessor = resourceAccessors.stream()
                .parallel()
                .filter(r->r.supportResourceType(resourceType))
                .findAny();
        if (!accessor.isPresent()){
            log.warn("资源类型 {} 未对应 ResourceAccessor",resourceType);
        }
        return accessor.orElse(denyResourceAccessor);
    }
}
