package com.qingfeng.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 封装用户登录参数的实体类
 *
 * @author 清风学Java
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserLogin {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}

