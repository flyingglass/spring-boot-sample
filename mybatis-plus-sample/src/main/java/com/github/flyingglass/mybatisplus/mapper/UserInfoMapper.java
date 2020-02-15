package com.github.flyingglass.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.flyingglass.mybatisplus.entity.UserInfo;
import com.github.flyingglass.mybatis.cache.MybatisRedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Property;
import org.redisson.mybatis.RedissonCache;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Generator
 * @since 2019-07-15
 */
//@CacheNamespace(
//        implementation = MybatisRedisCache.class,
//        properties = { @Property(
//                name = "flushInterval",
//                value = "10000"
//        )}
//)
@CacheNamespace(
        implementation = RedissonCache.class,
        properties = {
                @Property(
                        name = "redissonConfig",
                        value = "/redisson.yml"
                ),
                @Property(
                        name = "maxSize",
                        value = "100000"
                )

        }
)
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}
