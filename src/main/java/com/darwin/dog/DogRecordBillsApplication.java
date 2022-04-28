package com.darwin.dog;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import springfox.documentation.oas.annotations.EnableOpenApi;


@EnableWebSecurity(debug = true)
@EnableOpenApi
@EnableCaching
// @EnableRedisHttpSession
@SpringBootApplication
public class DogRecordBillsApplication {
    public static void main(String[] args) {
        SpringApplication.run(DogRecordBillsApplication.class, args);
    }
}
