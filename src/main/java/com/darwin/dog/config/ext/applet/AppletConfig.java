package com.darwin.dog.config.ext.applet;

import com.darwin.dog.base.login.applet.AppletAuthenticationProvider;
import com.darwin.dog.base.login.applet.AppletLoginConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(AppletConfigurationProperties.class)
public class AppletConfig {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AppletConfigurationProperties appletConfigurationProperties;

    public AppletAuthenticationProvider appletAuthenticationProvider(){
        AppletAuthenticationProvider appletAuthenticationProvider = new AppletAuthenticationProvider(restTemplate);
        appletAuthenticationProvider.setAppID(appletConfigurationProperties.getAppID());
        appletAuthenticationProvider.setGrantType(appletConfigurationProperties.getGrantType());
        appletAuthenticationProvider.setSecret(appletConfigurationProperties.getSecret());
        return appletAuthenticationProvider;
    }

    @Bean
    public SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> securityConfigurerAdapter(){
        return new AppletLoginConfigurer(restTemplate,appletAuthenticationProvider(),appletConfigurationProperties.getLoginPath());
    }
}
