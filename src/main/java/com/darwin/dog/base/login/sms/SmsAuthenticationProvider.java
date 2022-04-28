package com.darwin.dog.base.login.sms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.ClassUtils;


@Setter
@Getter
/**
 * 短信认证 Provider
 */
public class SmsAuthenticationProvider implements AuthenticationProvider {


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken smsAuthenticationToken = (SmsAuthenticationToken)authentication;
        return smsAuthenticationToken;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return ClassUtils.isAssignable(SmsAuthenticationToken.class,authentication);
    }
}
