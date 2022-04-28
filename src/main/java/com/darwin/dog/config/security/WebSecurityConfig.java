package com.darwin.dog.config.security;


import com.darwin.dog.base.login.HttpSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    List<? extends SecurityConfigurer<DefaultSecurityFilterChain,HttpSecurity>> securityConfigurers;

    @Autowired
    List<HttpSecurityConfigurer> httpSecurityConfigurers;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable();
        for (HttpSecurityConfigurer httpSecurityConfigurer:httpSecurityConfigurers){
            httpSecurityConfigurer.configure(http);
        }
        // 加载所有的 Configurer
        loadConfigurer(http);
    }


    private void loadConfigurer(HttpSecurity http) throws Exception {
        for (SecurityConfigurer<DefaultSecurityFilterChain,HttpSecurity> securityConfigurer:securityConfigurers){
            http.apply(securityConfigurer);
        }
    }
}
