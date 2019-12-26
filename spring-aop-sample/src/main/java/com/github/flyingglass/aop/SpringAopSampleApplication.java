package com.github.flyingglass.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author Administrator
 */
@Slf4j
@SpringBootApplication
public class SpringAopSampleApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(SpringAopSampleApplication.class, args);

        TestBean testBean = ctx.getBean("testBean", TestBean.class);

        log.info(testBean.getTag());
    }

}
