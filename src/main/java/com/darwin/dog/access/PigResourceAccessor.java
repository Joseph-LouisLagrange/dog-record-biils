package com.darwin.dog.access;

import com.darwin.dog.base.acl.rar.BasicResourceAccessor;
import com.darwin.dog.base.acl.rar.SimpleResourceTypeMatch;
import com.darwin.dog.po.sys.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.util.Set;

@Slf4j
@Component("pigResourceAccessor")
public class PigResourceAccessor extends BasicResourceAccessor {
    public PigResourceAccessor() {
        super(new SimpleResourceTypeMatch("Pig","com.xxx.pojo.Pig"));
    }

    @Override
    public boolean read(SysUser sysUser, Set<Long> resourceIDs) {
        log.debug("执行 read user:{} resourceIDs:{}", sysUser,resourceIDs);
        return true;
    }

    @Override
    public boolean create(SysUser sysUser, Set<Long> resourceIDs) {
        log.debug("执行 create user:{} resourceIDs:{}", sysUser,resourceIDs);
        return false;
    }

    @Override
    public boolean delete(SysUser sysUser, Set<Long> resourceIDs) {
        log.debug("执行 delete user:{} resourceIDs:{}", sysUser,resourceIDs);
        return false;
    }

    @Override
    public boolean update(SysUser sysUser, Set<Long> resourceIDs) {
        log.debug("执行 update user:{} resourceIDs:{}", sysUser,resourceIDs);
        return false;
    }

}
