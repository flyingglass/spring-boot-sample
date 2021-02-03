package com.github.flyingglass.mybatisplus.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
    @Accessors(chain = true, fluent = false)
    public class UserInfo implements Serializable {

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

    private Integer id;
    private Date gmtCreate;
    private Date gmtUpdated;
}
