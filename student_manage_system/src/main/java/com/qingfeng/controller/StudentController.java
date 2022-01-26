package com.qingfeng.controller;

import com.qingfeng.constants.UserConstant;
import com.qingfeng.entity.User;
import com.qingfeng.service.UserService;
import com.qingfeng.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 学生的控制层
 *
 * @author 清风学Java
 */
@RestController
@RequestMapping("/student")
public class StudentController {
    
    private UserService userService;

    @Autowired
    public StudentController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 异步加载学生列表
     * @param page  页码 默认是1
     * @param rows  每页显示的条目数 默认是20
     * @param classId  专业id 默认是0
     * @param studentName  学生姓名
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
        Map<String, Object> paramMap = new HashMap<>();
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
        //获取session中的user对象
        User loginUser = (User) session.getAttribute(UserConstant.LOGIN_USER);
        //判断是不是学生，学生只能查询自己的信息
        if (UserConstant.STUDENT_CODE.equals(loginUser.getUserType())) {
            // 只能查询自己的信息
            paramMap.put("studentId", loginUser.getId());
        }
        //判断是不是老师，老师只能查询自己的信息
        if (UserConstant.TEACHER_CODE.equals(loginUser.getUserType())) {
            paramMap.put("teacherId", loginUser.getId());
        }

        //分页查询数据
        PageBean<User> pageBean = userService.getStudentPage(paramMap);
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
}
