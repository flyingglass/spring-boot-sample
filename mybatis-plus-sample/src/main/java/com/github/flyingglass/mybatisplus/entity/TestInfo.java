package com.github.flyingglass.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.github.flyingglass.mybatis.config.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author ly
 * date 2019/5/22 17:52
 * description: phoenix hbase TEST_INFO table
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TestInfo extends BaseDO {

    /**
     * Phoenix主键Mybatis不支持自增，需要注解声明
     */
    @TableId(type = IdType.ID_WORKER)
    private Long id;

    private String name;
    private String phone;
    private String position;
    private String department;
    private String company;
    private String fileName;
    private String posDepCom;
}
