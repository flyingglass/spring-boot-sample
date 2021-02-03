package com.github.flyingglass.mybatisplus.service;


import com.baomidou.dynamic.datasource.support.DbHealthIndicator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fly
 * 多数据源探针
 * 1. DataSource为DynamicRoutingDataSource才会进行健康检测，其余一律不检查
 * 2. 非DynamicRoutingDataSource自己实现探针吧
 */
@Slf4j
@Service
public class DataSourceCheckServiceImpl implements IDataSourceCheckService {

    @Autowired
    private DbHealthIndicator dbHealthIndicator;
    /**
     * 数据库健康探针，启动也会进行检查
     * 1. 只有DataSource为DynamicRoutingDataSource才会进行健康
     * 2. 其他数据源自己实现吧
     * @return
     */
    @Override
    public void check() {
        log.info("================数据库检查begin==================");
        // 非DynamicRoutingDataSource数据源自己实现探针逻辑
        log.info(dbHealthIndicator.health().toString());
        log.info("================数据库检查end==================");
    }


}
