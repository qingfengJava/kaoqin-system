package com.qingfeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 专业实体类
 *
 * @author 清风学Java
 */
@Data
@TableName("s_clazz")
public class Clazz {

    /**
     * 专业 id 自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 专业名称
     */
    private String name;

    /**
     * 专业介绍
     */
    private String info;

}
