package com.github.flyingglass.mybatisplus.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.flyingglass.mybatisplus.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

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
//@CacheNamespace(
//        implementation = RedissonCache.class,
//        properties = {
//                @Property(
//                        name = "redissonConfig",
//                        value = "/redisson.yml"
//                ),
//                @Property(
//                        name = "maxSize",
//                        value = "100000"
//                )
//
//        }
//)
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * @param id
     * @return
     */
    @SelectKey(before = true, statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", resultType = String.class)
    @Update("Update user_info SET username='test' WHERE id=#{id}")
    Integer selectBeforeUpdate(@Param("id") int id);


    /**
     * @param id
     * @return
     */
    @SelectKey(before = false, statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", resultType = String.class)
    @Update("Update user_info SET username='test' WHERE id=#{id}")
    Integer selectAfterUpdate(@Param("id") int id);

    /**
     * @param id
     * @return
     */
    @Select("Select * from user_info where id =#{id}")
    UserInfo selectAuto(@Param("id") int id);

    @Master
    @Select("Select * from user_info where id =#{id}")
    UserInfo selectMaster(@Param("id") int id);

    @Slave
    @Select("Select * from user_info where id =#{id}")
    UserInfo selectSlave(@Param("id") int id);

}
