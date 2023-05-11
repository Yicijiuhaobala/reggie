package com.web.reggie;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
class ReggieApplicationTests {

    @Resource
    private RedisTemplate redisTemplate;
    @Test
    void contextLoads() {
        redisTemplate.opsForValue().set("sss", "wuhan");

        System.out.println(redisTemplate.type("sss"));

        redisTemplate.opsForHash().put("hash1", "name", "xiaoming");
        redisTemplate.opsForHash().put("hash1", "age", "20");
        System.out.println(redisTemplate.opsForHash().get("hash1", "age"));


    }

}
