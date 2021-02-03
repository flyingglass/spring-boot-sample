package com.github.flyingglass.mybatisplus.config;

import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationAdvisor;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import com.baomidou.dynamic.datasource.plugin.MasterSlaveAutoRoutingPlugin;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.support.DbHealthIndicator;
import com.baomidou.dynamic.datasource.support.HealthCheckAdapter;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author fly
 */
@Configuration
public class MasterSlaveConfiguration {

    @Resource
    DynamicDataSourceProperties properties;

    @Bean("dynamicDataSourceHealthCheck")
    @ConditionalOnMissingBean(DbHealthIndicator.class)
    public DbHealthIndicator healthIndicator(
            DataSource dataSource,
            DynamicDataSourceProperties dynamicDataSourceProperties,
            HealthCheckAdapter healthCheckAdapter
    ) {
        return new DbHealthIndicator(dataSource, dynamicDataSourceProperties.getHealthValidQuery(), healthCheckAdapter);
    }


    @Bean
    public MasterSlaveAutoRoutingPlugin masterSlaveAutoRoutingPlugin() {
        return new MasterSlaveAutoRoutingPlugin();
    }

}
