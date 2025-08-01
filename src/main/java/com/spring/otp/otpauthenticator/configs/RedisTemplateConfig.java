package com.spring.otp.otpauthenticator.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisTemplateConfig {

    @Bean
    public StringRedisTemplate getStringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
                    return new StringRedisTemplate(redisConnectionFactory);
    }
}
