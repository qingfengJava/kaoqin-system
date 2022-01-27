package com.qingfeng.controller;

import com.qingfeng.constants.UserConstant;
import com.qingfeng.entity.User;
import com.qingfeng.pojo.UserLogin;
import com.qingfeng.service.SelectedCourseService;
import com.qingfeng.service.UserService;
import com.qingfeng.utils.IdsData;
import com.qingfeng.utils.PageBean;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 异步加载用户列表
     * @param page  页码 默认是1
     * @param rows  每页显示的条目数 默认是20
     * @param classId  专业id 默认是0
     * @param studentName  用户姓名
     * @param form
     * @param session
     * @return
     */
    @RequestMapping("/list")
    public Object getStudentList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "rows", defaultValue = "20") Integer rows,
                                 @RequestParam(value = "classId", defaultValue = "0") String classId,
                                 String studentName,
                                 String form,
                                 HttpSession session) {
        //定义一个map集合用于存放数据
        Map<String, Object> paramMap = new HashMap<>(10);
        //分页的页码
        paramMap.put("pageno", page);
        //每页显示的条目数
        paramMap.put("pagesize", rows);

        //学生姓名
        if (!StringUtils.isEmpty(studentName)) {
            //如果有学生姓名 就放如map中， 全局搜索是没有的
            paramMap.put("username", studentName);
        }

        //专业Id
        if (!"0".equals(classId)) {
            //0只是表示在搜索时全局搜索   如果有专业id，那就放如map集合中
            paramMap.put("classId", classId);
        }

        //分页查询数据
        PageBean<User> pageBean = userService.getUserPage(paramMap);
        System.out.println(paramMap);
        //判断是不是首次分页查询数据
        if (!StringUtils.isEmpty(form) && "combox".equals(form)) {
            //不是首次，直接返回
            return pageBean.getDatas();
        } else {
            //首次 封装总记录数和显示的条目数
            Map<String, Object> result = new HashMap<>();
            result.put("total", pageBean.getTotalsize());
            result.put("rows", pageBean.getDatas());
            System.out.println(result);
            return result;
        }
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
        //注意：添加用户要保证用户名不能重复，所以要先查询用户是否存在
        User select_user = userService.getByUsername(user.getUsername());
        //为null说明改用户不存在，允许添加
        if (select_user == null){
            //调用service层添加用户的方法    i 表示影响的行数
            int i = userService.addUser(user);
            if (i > 0) {
                //返回封装的resultVo信息
                return ResultVO.success();
            }else {
                return ResultVO.fail("添加用户出现未知的异常！");
            }
        }
        return ResultVO.fail("添加失败，用户已存在");

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
        //根据id集合删除相关信息，返回的是影响的行数
        int count = userService.deleteUser(ids);
        if (count > 0) {
            return ResultVO.success();
        } else {
            return ResultVO.fail();

        }
    }
}
