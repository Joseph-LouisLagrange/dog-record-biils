package com.darwin.dog.base.login.applet;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Getter
@Setter
/**
 * 小程序认证器的 Provider
 */
public class AppletAuthenticationProvider implements AuthenticationProvider {

    RestTemplate restTemplate;

    public static final String APPID_KEY = "appid";

    public static final String SECRET_KEY = "secret";

    public static final String JSCODE_KEY = "js_code";

    public static final String GRANT_TYPE = "grant_type";

    private static final String CODE_TO_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={js_code}&grant_type={grant_type}";

    private String appID;

    private String secret;

    private String grantType;

    public AppletAuthenticationProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private Map<String,Object> getRequestParameterMap(String jsCode){
        Map<String,Object> requestParameterMap=new HashMap<>();
        requestParameterMap.put(APPID_KEY,appID);
        requestParameterMap.put(SECRET_KEY,secret);
        requestParameterMap.put(JSCODE_KEY,jsCode);
        requestParameterMap.put(GRANT_TYPE,grantType);
        return requestParameterMap;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AppletAuthenticationToken appletAuthenticationToken=(AppletAuthenticationToken)authentication;
        Map<String,Object> body=restTemplate.getForObject(CODE_TO_SESSION_URL,Map.class
                ,getRequestParameterMap(appletAuthenticationToken.getCode()));
        Assert.notNull(body,"Applet response body is null");
        Integer errCode = (Integer) body.get("errcode");
        AuthenticationException authenticationException = getAuthenticationException(errCode);
        if (Objects.nonNull(authenticationException)){
            throw authenticationException;
        }
        // 更新 appletAuthenticationToken 的认证状态
        updateAppletAuthenticationTokenState(appletAuthenticationToken,body);
        return appletAuthenticationToken;
    }


    private void updateAppletAuthenticationTokenState(AppletAuthenticationToken appletAuthenticationToken,Map<String,Object> body){
        appletAuthenticationToken.setAuthenticated(true);
        appletAuthenticationToken.setOpenID((String) body.get("openid"));
        appletAuthenticationToken.setSessionKey((String) body.get("sessionkey"));
        appletAuthenticationToken.setUnionID((String) body.get("unionid"));
    }

    private AuthenticationException getAuthenticationException(Integer errCode){
        switch (errCode){
            case -1:return new DisabledException("系统繁忙，此时请开发者稍候再试");
            case 40029:return new CredentialsExpiredException("code 无效");
            case 45011:return new DisabledException("频率限制，每个用户每分钟100次");
            case 40226:return new LockedException("高风险等级用户，小程序登录拦截");
            case 40013:return new DisabledException("无效的 appid");
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ClassUtils.isAssignable(AppletAuthenticationToken.class,authentication);
    }
}
