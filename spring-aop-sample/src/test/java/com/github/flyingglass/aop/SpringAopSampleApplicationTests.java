package com.github.flyingglass.aop;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
class SpringAopSampleApplicationTests {

    @Resource
    TestBean testBean;

    @Test
    void a00_contextLoads() {
        testBean.getName();
    }

}
