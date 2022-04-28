package com.darwin.dog.config.ext.applet;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "applet.auth")
public class AppletConfigurationProperties {
    private String appID;

    private String secret;

    private String grantType;

    private String loginPath;
}
