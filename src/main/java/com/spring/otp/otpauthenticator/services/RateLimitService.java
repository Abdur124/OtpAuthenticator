package com.spring.otp.otpauthenticator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public boolean isAllowed(String key, int maxRequests, Duration ttlInMinutes) {

        String redisKey = "rate_limit_" + key;

        Long count = stringRedisTemplate.opsForValue().increment(redisKey);

        if(count == 1) {
            stringRedisTemplate.expire(redisKey,ttlInMinutes);
        }

        return count <= maxRequests;
    }
}
