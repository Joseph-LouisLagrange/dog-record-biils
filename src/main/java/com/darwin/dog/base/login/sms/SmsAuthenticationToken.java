package com.darwin.dog.base.login.sms;

import lombok.*;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;




/**
 * 短信认证的 Token
 */
@Getter
@Setter
public class SmsAuthenticationToken extends AbstractAuthenticationToken {

    private String captcha;

    private String phone;

    public SmsAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    public SmsAuthenticationToken(String phone,String captcha){
        super(null);
        this.phone=phone;
        this.captcha=captcha;
    }

    @Override
    public Object getCredentials() {
        return captcha;
    }

    @Override
    public Object getPrincipal() {
        return phone;
    }

    public String getCaptcha() {
        return captcha;
    }
}
