package com.darwin.dog.base.login.sms;

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
 * 短信认证 Filter
 */
public class SmsAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private static final String PHONE_KEY = "phone";

    private static final String CAPTCHA_KEY = "captcha";


    protected SmsAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    protected SmsAuthenticationProcessingFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    private String getPhone(HttpServletRequest request){
        String phone = request.getParameter(PHONE_KEY);
        Assert.hasText(phone, "Phone parameter must not be empty or null");
        return phone;
    }

    private String getCaptcha(HttpServletRequest request){
        String phone = request.getParameter(CAPTCHA_KEY);
        Assert.hasText(phone, "Captcha parameter must not be empty or null");
        return phone;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        return getAuthenticationManager()
                .authenticate(new SmsAuthenticationToken(getPhone(request),getCaptcha(request)));
    }
}
