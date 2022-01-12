package com.qingfeng.controller;

import com.qingfeng.constants.UserConstant;
import com.qingfeng.pojo.UserLogin;
import com.qingfeng.entity.User;
import com.qingfeng.service.SelectedCourseService;
import com.qingfeng.service.UserService;
import com.qingfeng.utils.IdsData;
import com.qingfeng.utils.ResultVO;
import com.qingfeng.utils.SaltUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 用户控制层
 *
 * @author 清风学Java
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private SelectedCourseService selectedCourseService;

    /**
     *  通过构造器的方法注入对象
     */
    @Autowired
    public UserController(UserService userService, SelectedCourseService selectedCourseService) {
        this.userService = userService;
        this.selectedCourseService = selectedCourseService;
    }

    /**
     * 用户登录操作
     */
    @PostMapping("/login")
    public ResultVO<Boolean> login(UserLogin userLogin, HttpSession session) {
        try {
            // 使用Spring提供的工具类进行 空值判断
            if (StringUtils.isEmpty(userLogin.getUsername()) || StringUtils.isEmpty(userLogin.getPassword())) {
                //直接返回失败信息
                return ResultVO.fail("用户名或密码不能为空");
            }

            //不为空，进行登录判断
            //获取主体对象
            Subject subject = SecurityUtils.getSubject();
            //将用户登录的信息封装给token，用户进行登录校验
            UsernamePasswordToken token = new UsernamePasswordToken(userLogin.getUsername(), userLogin.getPassword());
            //进行登录校验，shiro底层会自动进行登录校验，失败抛出异常
            subject.login(token);
            // 没有抛异常说明登录成功，获取当前登录用户信息
            User user = userService.getByUsername(userLogin.getUsername());
            // 将用户信息存到 session 中
            session.setAttribute(UserConstant.LOGIN_USER, user);
            //返回信息
            return ResultVO.success();

        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            return ResultVO.fail("用户名或密码错误");
        }
    }

    /**
     * 添加用户的信息
     * @param user  待添加的用户信息
     * @return
     */
    @PostMapping("/add")
    public ResultVO<Boolean> addUser(User user) {
        //调用service层添加用户的方法    i 表示影响的行数
        int i = userService.addUser(user);
        if (i > 0) {
            //返回封装的resultVo信息
            return ResultVO.success();
        } else {
            return ResultVO.fail();
        }
    }

    /**
     * 修改用户信息
     * @param user  待修改的用户信息
     * @return
     */
    @PostMapping("/update")
    public ResultVO<Boolean> updateUser(User user) {
        //调用service修改用户的方法
        int i = userService.updateUser(user);
        if (i > 0) {
            //修改成功，返回ResultVO信息
            return ResultVO.success();
        } else {
            return ResultVO.fail();
        }
    }

    /**
     * 修改密码
     * @param password  原密码
     * @param newPassword  新密码
     * @param session
     * @return
     */
    @PostMapping("/password")
    public ResultVO<Boolean> editPassword(String password, String newPassword, HttpSession session) {
        // 通过session 获取当前登录用户
        User user = (User) session.getAttribute(UserConstant.LOGIN_USER);

        // 将输入的原密码加密与数据库中的密码对比
        password = new Md5Hash(password, user.getSalt(), 1024).toString();
        if (!password.equals(user.getPassword())) {
            return ResultVO.fail("原密码错误");
        }

        // 修改密码
        String salt = SaltUtil.getSalt(8);
        newPassword = new Md5Hash(newPassword, salt, 1024).toString();
        user.setSalt(salt);
        user.setPassword(newPassword);
        int count = userService.editPwd(user);
        if (count > 0) {
            return ResultVO.success("修改成功 ");
        } else {
            return ResultVO.fail("修改失败");
        }
    }

    /**
     * 根据id删除用户
     * @param data 用户id的集合
     * @return
     */
    @PostMapping("/delete")
    public ResultVO<Boolean> deleteStudent(IdsData data) {
        //获取id的集合
        List<Integer> ids = data.getIds();
        // 若删除的是学生，判断是否存在课程关联
        for (Integer id : ids) {
            if (!selectedCourseService.checkSelectedCourse(id)) {
                return ResultVO.fail("无法删除，存在课程关联学生");
            }
        }
        int count = userService.deleteUser(ids);
        if (count > 0) {
            return ResultVO.success();
        } else {
            return ResultVO.fail();

        }
    }
}
