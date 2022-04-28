package com.darwin.dog.base.login.applet;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 小程序的认证 Token
 */
@Setter
@Getter
public class AppletAuthenticationToken extends AbstractAuthenticationToken {

    private String code;

    private String openID;

    private String sessionKey;

    private String unionID;

    public AppletAuthenticationToken(){
        super(null);
    }

    public AppletAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    public AppletAuthenticationToken(String code){
        super(null);
        this.code=code;
    }

    @Override
    public Object getCredentials() {
        return code;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    public String getCode() {
        return code;
    }
}
