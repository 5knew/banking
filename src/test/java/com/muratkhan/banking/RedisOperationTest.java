package com.muratkhan.banking;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RedisOperationTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void testRedisOperation() {
        String key = "testKey";
        String expectedValue = "Hello, Redis!";
        redisTemplate.opsForValue().set(key, expectedValue);
        String actualValue = redisTemplate.opsForValue().get(key);
        assertEquals(expectedValue, actualValue, "The value retrieved from Redis should match the expected value.");
        redisTemplate.delete(key);
    }
}
