package com.qingfeng.controller;

import com.qingfeng.constants.UserConstant;
import com.qingfeng.entity.Attendance;
import com.qingfeng.entity.Course;
import com.qingfeng.entity.SelectedCourse;
import com.qingfeng.entity.User;
import com.qingfeng.service.AttendanceService;
import com.qingfeng.service.CourseService;
import com.qingfeng.service.SelectedCourseService;
import com.qingfeng.utils.DateFormatUtil;
import com.qingfeng.utils.PageBean;
import com.qingfeng.utils.ResultVO;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author 清风学Java
 */
@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final SelectedCourseService selectedCourseService;
    private final CourseService courseService;

    public AttendanceController(AttendanceService attendanceService, SelectedCourseService selectedCourseService, CourseService courseService) {
        this.attendanceService = attendanceService;
        this.selectedCourseService = selectedCourseService;
        this.courseService = courseService;
    }

    @GetMapping("/attendance_list")
    public String attendanceList() {
        return "/attendance/attendanceList";
    }

    /**
     * 异步获取考勤列表数据
     */
    @RequestMapping("/getAttendanceList")
    @ResponseBody
    public Object getAttendanceList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                    @RequestParam(value = "rows", defaultValue = "100") Integer rows,
                                    @RequestParam(value = "studentId", defaultValue = "0") String studentId,
                                    @RequestParam(value = "courseId", defaultValue = "0") String courseId,
                                    String type, String date, String from, HttpSession session) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pageno", page);
        paramMap.put("pagesize", rows);

        if (!"0".equals(studentId)) {
            paramMap.put("studentId", studentId);
        }
        if (!"0".equals(courseId)) {
            paramMap.put("courseId", courseId);
        }
        if (!StringUtils.isEmpty(type)) {
            paramMap.put("type", type);
        }
        if (!StringUtils.isEmpty(date)) {
            paramMap.put("date", date);
        }

        User loginUser = (User) session.getAttribute(UserConstant.LOGIN_USER);
        if (UserConstant.LOGIN_USER.equals(loginUser.getUserType())) {
            // 学生用户，只能查询自己的信息
            paramMap.put("studentId", loginUser.getId());
        }

        PageBean<Attendance> pageBean = attendanceService.queryPage(paramMap);
        if (!StringUtils.isEmpty(from) && "combox".equals(from)) {
            System.out.println(pageBean.getDatas());
            return pageBean.getDatas();
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("total", pageBean.getTotalsize());
            result.put("rows", pageBean.getDatas());
            System.out.println(result);
            return result;
        }
    }

    /**
     * 通过选课中的课程id查询学生所选择的课程
     */
    @RequestMapping("/getStudentSelectedCourseList")
    @ResponseBody
    public Object getStudentSelectedCourseList(@RequestParam(value = "studentId", defaultValue = "0") String studentId) {
        // 通过学生id查询选课信息
        List<SelectedCourse> selectedCourseList = selectedCourseService.getAllBySid(Integer.valueOf(studentId));
        // 通过选课中的课程id查询学生所选择的课程
        List<Integer> ids = new ArrayList<>();
        for (SelectedCourse selectedCourse : selectedCourseList) {
            ids.add(selectedCourse.getCourseId());
        }
        // 学生选课列表
        List<Course> courseById = courseService.getCourseById(ids);
        if (CollectionUtils.isEmpty(courseById)) {
            return ResultVO.fail("该学生未进行选课");
        }
        return courseById;
    }


    /**
     * 添加考勤签到
     */
    @PostMapping("/addAttendance")
    @ResponseBody
    public ResultVO<Boolean> addAttendance(Attendance attendance) {
        // 判断是否已签到
        boolean checkAttendance = attendanceService.checkAttendance(attendance);
        if (checkAttendance) {
            // true 为已签到
            return ResultVO.fail("已签到，请勿重复签到");
        } else {
            Course course = selectedCourseService.getCourseDetail(attendance.getStudentId(), attendance.getCourseId());
            if (Objects.nonNull(course)) {
                // 获取选择签到的课程的上课时间
                Date courseDate = DateFormatUtil.strToDate(course.getCourseDate());
                // 学生签到时间减 15 分钟大于上课时间则为迟到
                Calendar nowTime = Calendar.getInstance();
                nowTime.add(Calendar.MINUTE, -15);
                if (nowTime.getTime().after(courseDate)) {
                    attendance.setType("迟到");
                }
                // 保存签到信息
                attendance.setDate(new Date());
                int count = attendanceService.addAttendance(attendance);
                if (count > 0) {
                    return ResultVO.success();
                }
            }
            return ResultVO.fail();
        }
    }

    /**
     * 补签
     */
    @PostMapping("/reissue")
    @ResponseBody
    public ResultVO<Boolean> reissue(Integer id) {
        int count = attendanceService.reissue(id);
        if (count > 0) {
            return ResultVO.success();
        } else {
            return ResultVO.fail();
        }
    }
}
