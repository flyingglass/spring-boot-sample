package com.github.flyingglass.mybatisplus;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


/**
 * @author Administrator
 */
@Slf4j
@MapperScan(basePackages = "com.github.flyingglass.mybatisplus.mapper")
@SpringBootApplication(
        exclude = DruidDataSourceAutoConfigure.class
)
public class MybatisPlusSampleApplication {

    public static void main(String[] args) {
        // 设置HADOOP_HOME路径
        System.setProperty("hadoop.home.dir", System.getProperty("user.dir"));

        ApplicationContext applicationContext = SpringApplication.run(MybatisPlusSampleApplication.class, args);
    }

}
