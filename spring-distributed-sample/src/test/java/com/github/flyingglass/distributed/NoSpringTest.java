package com.github.flyingglass.distributed;

import com.baomidou.mybatisplus.core.toolkit.Sequence;
import com.github.flyingglass.distributed.chash.ConsistentHash;
import lombok.extern.slf4j.Slf4j;
import orestes.bloomfilter.BloomFilter;
import orestes.bloomfilter.FilterBuilder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: fly
 * Created date: 2019/12/27 11:46
 */
@Slf4j
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class NoSpringTest {

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
    void a01_00_bloomFilter() {
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
    void a03_consistentHash() {
        // 一致性hash

        log.info("一致性hash测试");
        ConsistentHash.testConsistentHash();

    }
}
