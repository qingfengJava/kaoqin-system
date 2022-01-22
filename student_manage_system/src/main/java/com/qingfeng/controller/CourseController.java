package com.qingfeng.controller;

import com.qingfeng.constants.UserConstant;
import com.qingfeng.entity.Course;
import com.qingfeng.entity.User;
import com.qingfeng.service.CourseService;
import com.qingfeng.utils.IdsData;
import com.qingfeng.utils.PageBean;
import com.qingfeng.utils.ResultVO;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 清风学Java
 */
@Controller
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/course_list")
    public String courseList() {
        return "course/courseList";
    }

    /**
     * 异步加载课程信息列表
     */
    @PostMapping("/getCourseList")
    @ResponseBody
    public Object getClazzList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "rows", defaultValue = "100") Integer rows,
                               @RequestParam(value = "weakday", defaultValue = "") String weakday,
                               String name,
                               @RequestParam(value = "teacherId", defaultValue = "0") String teacherId,
                               String from, HttpSession session) {
        Map<String, Object> paramMap = new HashMap<>(10);
        paramMap.put("pageno", page);
        paramMap.put("pagesize", rows);
        if (!StringUtils.isEmpty(name)) {
            paramMap.put("name", name);
        }
        //根据老师查询信息
        if (!"0".equals(teacherId)) {
            paramMap.put("teacherId", teacherId);
        }

        //按星期几查询
        if (!"".equals(weakday) && weakday != null){
            paramMap.put("weakday",weakday);
        }

        //判断是不是老师，是老师的话就只加载这个了老师下的课程
        //获取session中的user对象
        User loginUser = (User) session.getAttribute(UserConstant.LOGIN_USER);
        if (UserConstant.TEACHER_CODE.equals(loginUser.getUserType())){
            //说明是老师
            paramMap.put("teacherId", loginUser.getId());
        }
        //分页查询数据
        PageBean<Course> pageBean = courseService.queryPage(paramMap);
        if (!StringUtils.isEmpty(from) && "combox".equals(from)) {
            return pageBean.getDatas();
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("total", pageBean.getTotalsize());
            result.put("rows", pageBean.getDatas());
            return result;
        }
    }

    /**
     * 添加课程信息
     */
    @PostMapping("/addCourse")
    @ResponseBody
    public ResultVO<Boolean> addCourse(Course course,HttpSession session) {
        //将用户身份一起传过去，用户判断
        User loginUser = (User) session.getAttribute(UserConstant.LOGIN_USER);
        int count = courseService.addCourse(course,loginUser.getUserType());
        if (count > 0) {
            return ResultVO.success();
        } else {
            return ResultVO.fail();
        }
    }


    /**
     * 修改课程信息
     */
    @PostMapping("/editCourse")
    @ResponseBody
    public ResultVO<Boolean> editCourse(Course course,HttpSession session) {
        System.out.println(course);
        //将用户身份一起传过去，用户判断
        User loginUser = (User) session.getAttribute(UserConstant.LOGIN_USER);
        int count = courseService.editCourse(course,loginUser.getUserType());
        if (count > 0) {
            return ResultVO.success();
        } else {
            return ResultVO.fail();
        }
    }


    @PostMapping("/deleteCourse")
    @ResponseBody
    public ResultVO<Boolean> deleteCourse(IdsData data) {
        int count = courseService.deleteCourse(data.getIds());
        if (count > 0) {
            return ResultVO.success();
        } else {
            return ResultVO.fail();
        }
    }
}
