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
        //添加用户之前要检查用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, user.getUsername());
        List<User> users = userMapper.selectList(wrapper);
        if (users .size() == 0){
            //说明可以添加
            //给这个用户生成随机盐
            String salt = SaltUtil.getSalt(8);
            //使用shiro的md5hash加密算法对密码进行加密
            Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 1024);
            user.setSalt(salt);
            user.setPassword(md5Hash.toString());
            //调用mybatis-plus的插入方法
            return userMapper.insert(user);
        }

        //否则返回-1
        return -1;
    }

    /**
     * 根据Map中封装的数据，进行条件查询信息
     * @param paramMap 查询参数
     * @return
     */
    @Override
    public PageBean<User> getStudentPage(Map<String, Object> paramMap) {
        //声明一个pageBean对象
        PageBean<User> pageBean = new PageBean<>((Integer) paramMap.get("pageno"), (Integer) paramMap.get("pagesize"));
        //拿到起始页数据
        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex", startIndex);

        //学生的
        if (paramMap.get("teacherId") == null){
            // 获取所有学生列表
            List<User> users = userMapper.getStudentList(paramMap);
            pageBean.setDatas(users);
            Integer totalSize = userMapper.queryStuCount(paramMap);
            pageBean.setTotalsize(totalSize);
        }else{
            // 有老师的
            List<User> users = userMapper.getStudentListByTeacherId(paramMap);
            pageBean.setDatas(users);
            Integer totalSize = userMapper.queryStuCountByTeacherId(paramMap);
            pageBean.setTotalsize(totalSize);
        }

        return pageBean;
    }

    /**
     * 分页查询教师信息
     * @param paramMap 查询参数
     * @return
     */
    @Override
    public PageBean<User> getTeacherPage(Map<String, Object> paramMap) {
        PageBean<User> pageBean = new PageBean<>((Integer) paramMap.get("pageno"), (Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex", startIndex);

        // 获取所有教师列表
        List<User> users = userMapper.getTeacherList(paramMap);
        pageBean.setDatas(users);
        //查询总记录数
        Integer totalSize = userMapper.queryTenCount(paramMap);
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
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getClassId, id);
        List<User> users = userMapper.selectList(wrapper);
        if (users.size() == 0){
            //说明该专业下没有学生或者老师，可以删除
            return true;
        }
        //否则
        return false;
    }

    @Override
    public PageBean<User> getUserPage(Map<String, Object> paramMap) {
        //声明一个pageBean对象
        PageBean<User> pageBean = new PageBean<>((Integer) paramMap.get("pageno"), (Integer) paramMap.get("pagesize"));
        //拿到起始页数据
        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex", startIndex);
        List<User> users = userMapper.getUserList(paramMap);
        pageBean.setDatas(users);
        Integer totalSize = userMapper.queryUserCount(paramMap);
        pageBean.setTotalsize(totalSize);

        return pageBean;
    }

    @Override
    public int deleteUser(List<Integer> ids) {
        return userMapper.deleteBatchIds(ids);
    }

}
