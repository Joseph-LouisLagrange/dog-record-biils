package com.darwin.dog.base.login.applet;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 小程序认证处理 Filter
 */
public class AppletAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private static final String CODE_KEY="code";

    public AppletAuthenticationProcessingFilter(AuthenticationManager authenticationManager,String defaultFilterProcessesUrl){
        this(defaultFilterProcessesUrl);
        super.setAuthenticationManager(authenticationManager);
    }

    protected AppletAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    protected AppletAuthenticationProcessingFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    private String getCode(HttpServletRequest httpServletRequest){
        String code = httpServletRequest.getParameter(CODE_KEY);
        Assert.hasText(code, "Code parameter must not be empty or null");
        return code;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        return super.getAuthenticationManager()
                .authenticate(new AppletAuthenticationToken(getCode(httpServletRequest)));
    }

}
