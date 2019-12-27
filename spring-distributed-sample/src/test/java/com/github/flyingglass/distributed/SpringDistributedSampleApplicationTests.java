package com.github.flyingglass.distributed;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.github.flyingglass.distributed.lock.RedisSampleLock;
import lombok.extern.slf4j.Slf4j;
import orestes.bloomfilter.BloomFilter;
import orestes.bloomfilter.FilterBuilder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class SpringDistributedSampleApplicationTests {

    @Resource
    RedisProperties redisProperties;


    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void a00_snakeFlow() {
//        雪花算法
        int size = 10;


        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                50, 100,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>()
        );

        Sequence sequence = new Sequence();
        for (int i = 0; i < size; i++) {
            log.info("{}", sequence.nextId());
        }
    }

    @Test
    void a01_bloomFilter() {
//        布隆过滤器

        BloomFilter<String> bf = new FilterBuilder(1000, 0.1).buildBloomFilter();

        bf.add("Just");
        bf.add("Springboot +Mybatis-Plus + 多数据源 + Druid + Redis二级缓存");
        bf.add("Springboot");

        log.info(
                "{}:{}, {}, {}, {}",
                "布隆过滤器",
                bf.contains("Just"),
                bf.contains("Springboot +Mybatis-Plus + 多数据源 + Druid + Redis二级缓存"),
                bf.contains("Springboot"),
                bf.contains("404")
        );
    }

    @Test
    void a02_redisBloomFilter() {
//        Redis布隆过滤器
        FilterBuilder builder = new FilterBuilder(1000, 0.1)
                .redisHost(redisProperties.getHost())
                .redisPort(redisProperties.getPort());


        BloomFilter<String> bf = builder.buildBloomFilter();

        bf.add("Just");
        bf.add("Springboot +Mybatis-Plus + 多数据源 + Druid + Redis二级缓存");
        bf.add("Springboot");

        log.info(
                "{}:{}, {}, {}, {}",
                "Redis布隆过滤器",
                bf.contains("Just"),
                bf.contains("Springboot +Mybatis-Plus + 多数据源 + Druid + Redis二级缓存"),
                bf.contains("Springboot"),
                bf.contains("404")
        );
    }

    @Test
    void a03_redisDistributedLock() {
        // TODO 测试不通过，直接使用redisson
        RedisSampleLock lock = new RedisSampleLock(stringRedisTemplate, "lock", 10000L);
        log.info("{}", lock.tryLock());
    }
}
