package com.github.flyingglass.mybatisplus.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.flyingglass.mybatisplus.entity.UserInfo;
import com.github.flyingglass.mybatisplus.mapper.UserInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author: fly
 * Created date: 2019/12/23 16:39
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> {

    @Resource
    UserInfoMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    public String testTransaction() {

        Date now = new Date(System.currentTimeMillis());

        Integer count = mapper.selectCount(
                Wrappers.<UserInfo>lambdaQuery().select(UserInfo::getId)
        );

        String username = "test_" + count;

        UserInfo u = new UserInfo()
                .setUsername(username)
                .setNickname(username)
                .setPassword("123456")
                .setGmtCreate(now)
                .setGmtUpdated(now);
        save(u);

        log.info("插入完毕========");

        mapper.selectSlave(u.getId());

        return u.toString();
    }


    /**
     * 测试自动分流
     * @return
     */
    public String testDS() {

        mapper.selectMaster(1);
        mapper.selectSlave(2);
        mapper.selectMaster(3);
        mapper.selectSlave(4);
        mapper.selectAuto(5);
        mapper.selectById(6);

        mapper.selectOne( Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getId, 7) );

        return getOne(
                Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUsername, "test"), false
        ).toString();
    }


}
