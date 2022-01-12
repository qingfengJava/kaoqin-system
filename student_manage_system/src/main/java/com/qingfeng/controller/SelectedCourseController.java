package com.qingfeng.controller;

import com.qingfeng.constants.UserConstant;
import com.qingfeng.entity.SelectedCourse;
import com.qingfeng.entity.User;
import com.qingfeng.service.SelectedCourseService;
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
@RequestMapping("/selectedCourse")
public class SelectedCourseController {

    private final SelectedCourseService selectedCourseService;

    public SelectedCourseController(SelectedCourseService selectedCourseService) {
        this.selectedCourseService = selectedCourseService;
    }


    @GetMapping("/selectedCourse_list")
    public String selectedCourseList() {
        return "course/selectedCourseList";
    }

    /**
     * 异步加载选课信息列表
     */
    @PostMapping("/getSelectedCourseList")
    @ResponseBody
    public Object getClazzList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "rows", defaultValue = "100") Integer rows,
                               @RequestParam(value = "teacherid", defaultValue = "0") String studentid,
                               @RequestParam(value = "teacherid", defaultValue = "0") String courseid, String from, HttpSession session) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pageno", page);
        paramMap.put("pagesize", rows);
        if (!"0".equals(studentid)) {
            paramMap.put("studentId", studentid);
        }
        if (!"0".equals(courseid)) {
            paramMap.put("courseId", courseid);
        }
        //判断是老师还是学生权限
        User user = (User) session.getAttribute(UserConstant.LOGIN_USER);
        if (!UserConstant.STUDENT_CODE.equals(user.getUserType())) {
            //是学生权限，只能查询自己的信息
            paramMap.put("studentId", user.getId());
        }
        PageBean<SelectedCourse> pageBean = selectedCourseService.queryPage(paramMap);
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
     * 学生进行选课
     */
    @PostMapping("/addSelectedCourse")
    @ResponseBody
    public ResultVO<Boolean> addSelectedCourse(SelectedCourse selectedCourse) {
        int count = selectedCourseService.addSelectedCourse(selectedCourse);
        if (count == 1) {
            return ResultVO.success();
        } else if (count == 0) {
            return ResultVO.fail("选课人数已满");
        } else if (count == 2) {
            return ResultVO.fail("已选过这门课程");
        }
        return ResultVO.fail("系统繁忙");
    }


    /**
     * 删除选课信息
     */
    @PostMapping("/deleteSelectedCourse")
    @ResponseBody
    public ResultVO<Boolean> deleteSelectedCourse(Integer id) {
        int count = selectedCourseService.deleteSelectedCourse(id);
        if (count > 0) {
            return ResultVO.success();
        } else {
            return ResultVO.fail();
        }
    }
}
