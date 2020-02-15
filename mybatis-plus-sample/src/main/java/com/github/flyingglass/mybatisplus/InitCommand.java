package com.github.flyingglass.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.flyingglass.mybatisplus.entity.UserInfo;
import com.github.flyingglass.mybatisplus.mapper.UserInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fly
 * 2020/2/15 13:57
 */
@Slf4j
@Component
public class InitCommand implements CommandLineRunner {
    @Resource
    UserInfoMapper userInfoMapper;

    @Override
    public void run(String... args) throws Exception {
        /**
         * MySQL
         */
        final int size = 100;

        List<UserInfo> list = userInfoMapper.selectList(Wrappers.emptyWrapper());

//        list.stream().map(UserInfo::toString).forEach(log::info);

        while (true) {
            Thread.sleep(5000L);

            userInfoMapper.selectList(Wrappers.emptyWrapper());
        }
    }

    private void initUserInfo() {
        for (int i = 0; i < 20; i++) {
            UserInfo userInfo = new UserInfo()
                    .username("15999949014")
                    .password("123456")
                    .nickname("fly"+(i+1));

            userInfoMapper.insert(userInfo);
        }
    }
}
