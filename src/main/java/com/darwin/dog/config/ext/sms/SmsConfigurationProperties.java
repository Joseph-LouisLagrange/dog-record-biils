package com.darwin.dog.config.ext.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "sms.aliyun")
public class SmsConfigurationProperties {
    private String accessKeyID;

    private String accessKeySecret;
}
