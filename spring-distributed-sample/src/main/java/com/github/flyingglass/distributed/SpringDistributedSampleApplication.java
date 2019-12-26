package com.github.flyingglass.distributed;

import com.github.flyingglass.distributed.lock.RedisSampleLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Administrator
 */
@SpringBootApplication
public class SpringDistributedSampleApplication {


    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SpringDistributedSampleApplication.class, args);

        StringRedisTemplate redisTemplate = ctx.getBean("stringRedisTemplate", StringRedisTemplate.class);

        RedisSampleLock lock = new RedisSampleLock(redisTemplate, "lock", 10000L);

        lock.lock();


    }

}
