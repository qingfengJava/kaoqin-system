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
import com.qingfeng.utils.IdsData;
import com.qingfeng.utils.PageBean;
import com.qingfeng.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
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

    @Autowired
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
                                    String type, Attendance attendance, String from, HttpSession session) {

        Map<String, Object> paramMap = new HashMap<>(10);
        paramMap.put("pageno", page);
        paramMap.put("pagesize", rows);

        //先给老师Id指定一个空值
        paramMap.put("teacherId", "");

        if (!"0".equals(studentId)) {
            paramMap.put("studentId", studentId);
        }
        if (!"0".equals(courseId)) {
            paramMap.put("courseId", courseId);
        }
        if (!StringUtils.isEmpty(type)) {
            paramMap.put("type", type);
        }
        if (attendance.getCourseDate() != null) {
            paramMap.put("courseDate", attendance.getCourseDate());
        }

        User loginUser = (User) session.getAttribute(UserConstant.LOGIN_USER);
        if (UserConstant.STUDENT_CODE.equals(loginUser.getUserType())) {
            // 学生用户，只能查询自己的信息
            paramMap.put("studentId", loginUser.getId());
        }
        if (UserConstant.TEACHER_CODE.equals(loginUser.getUserType())) {
            // 老师只能加载自己学生的考勤信息
            paramMap.put("teacherId", loginUser.getId());
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
    public Object getStudentSelectedCourseList(@RequestParam(value = "studentId", defaultValue = "0") String studentId,HttpSession session) {
        // 通过学生id查询选课信息
        List<SelectedCourse> selectedCourseList = selectedCourseService.getAllBySid(Integer.valueOf(studentId));
        // 通过选课中的课程id查询学生所选择的课程
        List<Integer> ids = new ArrayList<>();
        for (SelectedCourse selectedCourse : selectedCourseList) {
            ids.add(selectedCourse.getCourseId());
        }
        //要查询对应老师下的课程
        User loginUser = (User) session.getAttribute(UserConstant.LOGIN_USER);
        int teacherId = 0;
        if(UserConstant.TEACHER_CODE.equals(loginUser.getUserType())){
            teacherId = loginUser.getId();
        }
        // 学生选课列表
        List<Course> courseById = courseService.getCourseById(ids,teacherId);
        if (CollectionUtils.isEmpty(courseById)) {
            return ResultVO.fail("该学生未进行选课");
        }
        System.out.println(courseById);
        return courseById;
    }


    /**
     * 添加考勤签到
     */
    @PostMapping("/addAttendance")
    @ResponseBody
    public ResultVO<Boolean> addAttendance(Attendance attendance,HttpSession session) throws ParseException {
        System.out.println(attendance);
        // 判断是否已签到
        boolean checkAttendance = attendanceService.checkAttendance(attendance);
        if (checkAttendance) {
            // true 为已签到
            return ResultVO.fail("签到信息有误或重复信息签到，请检查！");
        } else {
            //进行签到处理
            Course course = selectedCourseService.getCourseDetail(attendance.getStudentId(), attendance.getCourseId());
            if (Objects.nonNull(course)) {
                // 获取选择签到的课程的上课时间
                Date courseDate = DateFormatUtil.strToDate(course.getCourseDate().substring(0,course.getCourseDate().indexOf("~")));
                // 学生签到时间减 15 分钟大于上课时间则为迟到
                Calendar nowTime = Calendar.getInstance();
                nowTime.add(Calendar.MINUTE, -15);
                //当前时间减去15分钟还比上课时间大，说明迟到
                if (nowTime.getTime().after(courseDate)) {
                    attendance.setType("迟到");
                }

                //并且学生最多只能提前10分钟开始签到
                Calendar nowTime2 = Calendar.getInstance();
                //当前时间加上10分钟还小于上课时间，那么说明签到时间太早，不允许签到
                nowTime2.add(Calendar.MINUTE, +10);
                if (nowTime.getTime().before(courseDate)) {
                    return ResultVO.fail("还没到上课时间，请勿提前签到！");
                }

                // 保存签到信息
                attendance.setDate(new Date());

                //如果是学生签到，只能签到当天课程的信息
                User loginUser = (User) session.getAttribute(UserConstant.LOGIN_USER);
                if (UserConstant.STUDENT_CODE.equals(loginUser.getUserType())) {
                    // 学生用户 对签到的时间进行比较
                    if (attendance.getDate().toString().startsWith(attendance.getCourseDate().toString())){
                        //说明是同一天
                        int count = attendanceService.addAttendance(attendance);
                        if (count > 0) {
                            return ResultVO.success();
                        }
                    } else{
                        return ResultVO.fail("只能签到当天的课程！");
                    }
                }else if (UserConstant.TEACHER_CODE.equals(loginUser.getUserType())) {
                    //老师可以直接进行签到
                    int count = attendanceService.addAttendance(attendance);
                    if (count > 0) {
                        return ResultVO.success();
                    }
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
            return ResultVO.fail("服务器异常，补签失败！！！");
        }
    }

    /**
     * 删除考勤信息
     * @param data
     * @return
     */
    @PostMapping("/deleteAttendance")
    @ResponseBody
    public ResultVO<Boolean> deleteAttendance(IdsData data){
        //根据Id删除考勤信息
        int count = attendanceService.deleteList(data.getIds());
        if (count > 0) {
            return ResultVO.success();
        } else {
            return ResultVO.fail("服务器异常，删除考勤信息失败");
        }
    }

    /**
     * 修改考勤信息
     * @param attendance
     * @return
     */
    @PostMapping("/updateAttendance")
    @ResponseBody
    public ResultVO<Boolean> updateAttendance(Attendance attendance){
        int count = attendanceService.updateAttendance(attendance);
        if (count > 0) {
            return ResultVO.success();
        } else {
            return ResultVO.fail("服务器异常，修改考勤信息失败");
        }
    }
}
