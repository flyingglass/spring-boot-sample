package com.github.flyingglass.mybatisplus.entity;

import com.github.flyingglass.mybatis.config.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* <p>
    * 用户表
    * </p>
*
* @author Generator
* @since 2019-07-15
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true, fluent = true)
    public class UserInfo extends BaseDO {

    private static final long serialVersionUID = 1L;

            /**
            * 用户名
            */
    private String username;

            /**
            * 密码
            */
    private String password;

    private String nickname;

    private Boolean deleted;
}
