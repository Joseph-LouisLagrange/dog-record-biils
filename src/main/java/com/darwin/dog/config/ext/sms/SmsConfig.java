package com.darwin.dog.config.ext.sms;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SmsConfigurationProperties.class)
public class SmsConfig {
    @Autowired
    SmsConfigurationProperties smsConfigurationProperties;

    @Bean
    public Client smsClient() throws Exception{
        Config config = new Config();
        config
                .setEndpoint("dysmsapi.aliyuncs.com")
                .setAccessKeyId(smsConfigurationProperties.getAccessKeyID())
                .setAccessKeySecret(smsConfigurationProperties.getAccessKeySecret());
        return new Client(config);
    }
}
