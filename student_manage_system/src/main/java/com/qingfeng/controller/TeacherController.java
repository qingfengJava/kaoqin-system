package com.qingfeng.controller;

import com.qingfeng.constants.UserConstant;
import com.qingfeng.entity.User;
import com.qingfeng.service.UserService;
import com.qingfeng.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 清风学Java
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private UserService userService;

    /**
     * 异步加载老师数据列表
     */
    @PostMapping("/list")
    public Object getTeacherList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "rows", defaultValue = "100") Integer rows,
                                 @RequestParam(value = "classId", defaultValue = "0") String classId,
                                 String from, HttpSession session, String teacherName) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pageno", page);
        paramMap.put("pagesize", rows);

        if (!StringUtils.isEmpty(teacherName)) {
            paramMap.put("username", teacherName);
        }

        if (!"0".equals(classId)) {
            paramMap.put("classId", classId);
        }

        // 获取登录用户的用户类型
        User loginUser = (User) session.getAttribute(UserConstant.LOGIN_USER);
        if (UserConstant.TEACHER_CODE.equals(loginUser.getUserType())) {
            // 如果是教师类型，只能查看自己的信息
            paramMap.put("teacherId", loginUser.getId());
        }

        PageBean<User> pageBean = userService.getTeacherPage(paramMap);
        if (!StringUtils.isEmpty(from) && "combox".equals(from)) {
            return pageBean.getDatas();
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("total", pageBean.getTotalsize());
            result.put("rows", pageBean.getDatas());
            return result;
        }
    }
}
