package com.qingfeng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 清风学Java
 */
@SpringBootApplication
@MapperScan("com.qingfeng.mapper")
public class StudentManageSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentManageSystemApplication.class, args);
    }

}
