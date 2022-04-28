package com.darwin.dog.base.login.basic;

import com.darwin.dog.base.login.HttpSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

@Component
public class HttpBasicAuthenticationConfigurer implements HttpSecurityConfigurer {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic().realmName("dog");
    }
}
