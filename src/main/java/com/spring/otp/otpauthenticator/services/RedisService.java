package com.spring.otp.otpauthenticator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void cacheOtp(String aadhaar, String otp, long ttlInMinutes) {
        stringRedisTemplate.opsForValue().set(aadhaar, otp, Duration.ofMinutes(ttlInMinutes));
    }

    public String getOtp(String aadhaar) {
        return stringRedisTemplate.opsForValue().get(aadhaar);
    }

    public void deleteOtp(String aadhaar) {
        stringRedisTemplate.delete(aadhaar);
    }
}
