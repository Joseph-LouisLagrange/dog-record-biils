package com.darwin.dog.base.login.applet;

import com.darwin.dog.config.security.handler.SimpleAuthFailureHandler;
import com.darwin.dog.config.security.handler.SimpleAuthSuccessHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.client.RestTemplate;

import javax.servlet.Filter;

/**
 * 小程序认证的 Configurer
 */
public class AppletLoginConfigurer<T extends AppletLoginConfigurer<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {

    private RestTemplate restTemplate;

    private String authPath;

    private AppletAuthenticationProvider appletAuthenticationProvider;

    public AppletLoginConfigurer(RestTemplate restTemplate,AppletAuthenticationProvider appletAuthenticationProvider,String authPath) {
        this.restTemplate = restTemplate;
        this.authPath = authPath;
        this.appletAuthenticationProvider=appletAuthenticationProvider;
    }

    @Override
    public void configure(B http) throws Exception {
        http.authenticationProvider(appletAuthenticationProvider);
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilterAfter(getFilter(authenticationManager), SessionManagementFilter.class);
    }

    private Filter getFilter(AuthenticationManager authenticationManager){
        AppletAuthenticationProcessingFilter filter = new AppletAuthenticationProcessingFilter(authenticationManager,authPath);
        filter.setAllowSessionCreation(true);
        filter.setAuthenticationSuccessHandler(new SimpleAuthSuccessHandler());
        filter.setAuthenticationFailureHandler(new SimpleAuthFailureHandler());
        return filter;
    }
}
