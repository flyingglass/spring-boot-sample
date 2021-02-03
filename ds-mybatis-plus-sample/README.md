# mybatis-plus-starter-demo

## 简介

该项目为[redis-mybatis-plus-starter](https://github.com/FlyingGlass/redis-mybatis-plus-starter)和[phoenix-mybatis-plus-starter](https://github.com/FlyingGlass/phoenix-mybatis-plus-starter)的`demo`项目


### 版本基础
- Mybatis-plus: 3.2.0
- Spring-Boot: 2.2.2.RELEASE
- Druid: 1.1.16
- Phoenix: 5.0.0-HBase-2.0 
- HBase: 2.1.0-cdh6.3.0 

### Quick-Start

- 在`hosts`中配置`zookeeper`节点映射`ip`
- 配置`hbase-site.xml`，项目默认开启了`Phoenix Namespace`
- 修正`application.yml`中的`mysql`和`redis`信息，保证`mysql、redis、phoenix`可连接以及对应的`Schema`存在
- 运行命令`mvnw clean spring-boot:run`

### 样例代码说明

- `UserInfoMapper`主要展示了如何配置`Redis`作为`Mybatis`的二级缓存，以及多数据源切换使用
- `TestInfoMapper`主要展示了如何通过`Mybatis-Plus`使用`Phoenix`和`Redis`缓存

### 参考链接
- [Phoenix Reference](https://phoenix.apache.org/)
- [redis-mybatis-plus-starter](https://github.com/FlyingGlass/redis-mybatis-plus-starter)
- [phoenix-mybatis-plus-starter](https://github.com/FlyingGlass/phoenix-mybatis-plus-starter)