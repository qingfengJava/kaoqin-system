package com.qingfeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户实体类
 * 
 * 用户包括：学生、教师、辅导员、教导主任、管理员
 *
 * @author 清风学Java
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("s_user")
public class User {

    /**
     * 管理员 id 自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 用户类型
     * 1：管理员
     * 2：学生
     * 3：教师
     */
    private String userType;

    /**
     * 姓名
     */
    private String nickName;

    /**
     * 专业 id
     */
    private Integer classId;

    /**
     * 联系方式
     */
    private String mobile;

    /**
     * 性别
     */
    private String sex = "男";

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDeleted;

}
