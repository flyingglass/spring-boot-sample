package com.github.flyingglass.mybatisplus;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author Administrator
 */
@Slf4j
@EnableSwagger2
@MapperScan(basePackages = "com.github.flyingglass.mybatisplus.mapper")
@SpringBootApplication(
        exclude = DruidDataSourceAutoConfigure.class
)
@EnableScheduling
public class DsMybatisPlusSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DsMybatisPlusSampleApplication.class, args);
    }

}
