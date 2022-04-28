package com.darwin.dog.config;


import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean("redisCacheManager")
    public CacheManager redisCacheManager(@Autowired RedisConnectionFactory connectionFactory){
        return RedisCacheManager.builder(connectionFactory)
                .cacheWriter(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory))
                .build();
    }

    @Primary    // 设置为主缓存，表示在缺省的情况下走 LocalCache
    @Bean("caffeineCacheManager")
    public CacheManager caffeineCacheManager(){
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setAllowNullValues(false);
        caffeineCacheManager.registerCustomCache("permission-cache",
                Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .maximumSize(100L)
                .softValues().build());
        return caffeineCacheManager;
    }

}
