package com.qingfeng;

import com.qingfeng.entity.User;
import com.qingfeng.mapper.UserMapper;
import com.qingfeng.utils.SaltUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class StudentManageSystemApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {
        User user = new User();
        user.setPassword("1");
        user.setUsername("1");
        user.setUserType("3");
        user.setNickName("教师");
        user.setMobile("13131313311");
        user.setSex("男");
        user.setCreateDate(new Date());
        user.setIsDeleted(0);

        String salt = SaltUtil.getSalt(8);
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 1024);
        user.setSalt(salt);
        user.setPassword(md5Hash.toString());
        System.out.println(userMapper.insert(user));
    }

}
