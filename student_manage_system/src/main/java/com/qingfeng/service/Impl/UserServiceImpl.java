package com.qingfeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qingfeng.entity.User;
import com.qingfeng.mapper.UserMapper;
import com.qingfeng.service.UserService;
import com.qingfeng.utils.PageBean;
import com.qingfeng.utils.SaltUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户业务层接口实现
 *
 * @author 清风学Java
 */
@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public int editPwd(User user) {
        return userMapper.updateById(user);
    }

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return
     */
    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username);
        //根据封装的信息查询用户
        return userMapper.selectOne(wrapper);
    }

    @Override
    public int addUser(User user) {
        //给这个用户生成随机盐
        String salt = SaltUtil.getSalt(8);
        //使用shiro的md5hash加密算法对密码进行加密
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 1024);
        user.setSalt(salt);
        user.setPassword(md5Hash.toString());
        //调用mybatis-plus的插入方法
        return userMapper.insert(user);
    }

    @Override
    public PageBean<User> getStudentPage(Map<String, Object> paramMap) {
        PageBean<User> pageBean = new PageBean<>((Integer) paramMap.get("pageno"), (Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex", startIndex);

        // 获取所有学生列表
        List<User> users = userMapper.getStudentList(paramMap);
        pageBean.setDatas(users);

        Integer totalSize = userMapper.queryCount(paramMap);
        pageBean.setTotalsize(totalSize);
        return pageBean;
    }

    @Override
    public PageBean<User> getTeacherPage(Map<String, Object> paramMap) {
        PageBean<User> pageBean = new PageBean<>((Integer) paramMap.get("pageno"), (Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex", startIndex);

        // 获取所有学生列表
        List<User> users = userMapper.getTeacherList(paramMap);
        pageBean.setDatas(users);

        Integer totalSize = userMapper.queryCount(paramMap);
        pageBean.setTotalsize(totalSize);
        return pageBean;
    }

    @Override
    public int updateUser(User user) {
        //调用mybatis-plus的根据Id修改信息的方法
        return userMapper.updateById(user);
    }

    @Override
    public boolean checkStudentInCourse(Integer id) {
        return false;
    }

    @Override
    public int deleteUser(List<Integer> ids) {
        return userMapper.deleteBatchIds(ids);
    }

}
