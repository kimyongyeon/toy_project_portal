package com.simple.portal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

@SpringBootTest
public class PortalApplicationTests {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void contextLoads() {
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        setOperations.add("kkkkk", "1");
        setOperations.add("aaaa", "2");
    }
}
