package com.darwin.dog.service.inf.sys;

import com.darwin.dog.po.User;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public interface BasicService {
    default User getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return null;
        User user = null;
        if (authentication.isAuthenticated()) {
            if (ClassUtils.isAssignable(authentication.getPrincipal().getClass(), User.class)) {
                user = (User) authentication.getPrincipal();
            } else if (ClassUtils.isAssignable(authentication.getDetails().getClass(), User.class)) {
                user = (User) authentication.getDetails();
            }
        }
        return user;
    }
}
