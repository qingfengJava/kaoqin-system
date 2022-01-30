package com.qingfeng.controller;

import com.qingfeng.constants.UserConstant;
import com.qingfeng.entity.Course;
import com.qingfeng.entity.SelectedCourse;
import com.qingfeng.entity.User;
import com.qingfeng.service.CourseService;
import com.qingfeng.service.SelectedCourseService;
import com.qingfeng.utils.PageBean;
import com.qingfeng.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 清风学Java
 */
@Controller
@RequestMapping("/selectedCourse")
public class SelectedCourseController {

    private SelectedCourseService selectedCourseService;
    private CourseService courseService;

    @Autowired
    public SelectedCourseController(SelectedCourseService selectedCourseService, CourseService courseService) {
        this.selectedCourseService = selectedCourseService;
        this.courseService = courseService;
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
                               @RequestParam(value = "studentId", defaultValue = "0") String studentId,
                               @RequestParam(value = "courseId", defaultValue = "0") String courseId, String from, HttpSession session) {
        Map<String, Object> paramMap = new HashMap<>(16);
        paramMap.put("pageno", page);
        paramMap.put("pagesize", rows);

        //判断有没有学生Id这个信息，有就要根据这个信息进行查询
        if (!"0".equals(studentId)) {
            paramMap.put("studentId", studentId);
        }
        //判断有没有课程Id这个信息，有就要根据课程Id进行查询
        if (!"0".equals(courseId)) {
            paramMap.put("courseId", courseId);
        }
        //判断是老师还是学生权限
        User user = (User) session.getAttribute(UserConstant.LOGIN_USER);
        if (UserConstant.STUDENT_CODE.equals(user.getUserType())) {
            //是学生权限，只能查询自己的信息
            paramMap.put("studentId", user.getId());
        }
        if(UserConstant.TEACHER_CODE.equals(user.getUserType())){
            //教师只能查询自己课程下的信息
            paramMap.put("teacherId", user.getId());
        }
        PageBean<SelectedCourse> pageBean = selectedCourseService.queryPage(paramMap);
        System.out.println(paramMap);
        System.out.println("结果："+pageBean.getDatas());
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
        //先根据课程Id吧对应的教室id查询出来
        List<Integer> ids = new ArrayList<>();
        ids.add(selectedCourse.getCourseId());
        List<Course> courseById = courseService.getCourseById(ids);
        //因为只有一个Id，因此只会查询到一个数据
        for (Course course : courseById) {
            selectedCourse.setTeacherId(course.getTeacherId());
        }

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
        //根据Id删除选课信息
        int count = selectedCourseService.deleteSelectedCourse(id);
        if (count > 0) {
            return ResultVO.success("退课成功");
        } else {
            return ResultVO.fail("退课失败");
        }
    }
}
