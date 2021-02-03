package com.github.flyingglass.mybatisplus.web;

import com.github.flyingglass.mybatisplus.service.UserInfoServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author fly
 */
@RestController
@RequestMapping("test")
public class TestController {
    @Resource
    UserInfoServiceImpl service;

    /**
     * 测试DS切换情况
     * @return
     */
    @GetMapping("/ds")
    String testDS() {
        return service.testDS();
    }

    /**
     * 测试Mybatis-Plus的BaseMapper自动分流
     * @return
     */
    @GetMapping("/auto")
    String testAuto() {
        return service.getById(1).toString();
    }

    /**
     * 测试事务
     * @return
     */
    @GetMapping("/trans")
    String testTrans() {
        return service.testTransaction();
    }
}
