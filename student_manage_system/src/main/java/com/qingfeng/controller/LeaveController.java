package com.qingfeng.controller;

import com.qingfeng.constants.UserConstant;
import com.qingfeng.entity.Leave;
import com.qingfeng.entity.User;
import com.qingfeng.service.LeaveService;
import com.qingfeng.utils.PageBean;
import com.qingfeng.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 清风学Java
 */
@Controller
@RequestMapping("/leave")
public class LeaveController {

    private final LeaveService leaveService;

    @Autowired
    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @RequestMapping("leave_list")
    public String leaveList() {
        return "/leave/leaveList";
    }

    /**
     * 异步加载请假列表
     */
    @PostMapping("/getLeaveList")
    @ResponseBody
    public Object getClazzList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "rows", defaultValue = "100") Integer rows,
                               @RequestParam(value = "studentId", defaultValue = "0") String studentId,
                               @RequestParam(value = "courseId", defaultValue = "0") String courseId,
                               String from, HttpSession session) {
        //封装要查询的一些信息
        Map<String, Object> paramMap = new HashMap<>(10);
        paramMap.put("pageno", page);
        paramMap.put("pagesize", rows);
        if (!"0".equals(studentId)) {
            paramMap.put("studentId", studentId);
        }
        if (!"0".equals(courseId)) {
            paramMap.put("courseId", courseId);
        }

        //加载请假列表  学生只加载自己下的请假列表，老师只加载自己学生的请假列表
        User loginUser = (User) session.getAttribute(UserConstant.LOGIN_USER);
        if (UserConstant.STUDENT_CODE.equals(loginUser.getUserType())) {
            // 学生用户，只能查询自己的请假信息
            paramMap.put("studentId", loginUser.getId());
        }
        if (UserConstant.TEACHER_CODE.equals(loginUser.getUserType())) {
            // 老师只能加载自己学生的请假信息
            paramMap.put("teacherId", loginUser.getId());
        }

        PageBean<Leave> pageBean = leaveService.queryPage(paramMap);
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
     * 添加学生请假条
     */
    @PostMapping("/addLeave")
    @ResponseBody
    public ResultVO<Boolean> addLeave(Leave leave) {
        int count = leaveService.addLeave(leave);
        if (count > 0) {
            return ResultVO.success();
        } else {
            return ResultVO.fail();
        }
    }

    /**
     * 修改请假条
     */
    @PostMapping("/editLeave")
    @ResponseBody
    public ResultVO<Boolean> editLeave(Leave leave) {
        int count = leaveService.editLeave(leave);
        if (count > 0) {
            return ResultVO.success();
        } else {
            return ResultVO.fail();
        }
    }

    /**
     * 对假条进行审核
     */
    @PostMapping("/checkLeave")
    @ResponseBody
    public ResultVO<Boolean> checkLeave(Leave leave) {
        int count = leaveService.checkLeave(leave);
        if (count > 0) {
            return ResultVO.success();
        } else {
            return ResultVO.fail();
        }
    }


    /**
     * 删除假条
     */
    @PostMapping("/deleteLeave")
    @ResponseBody
    public ResultVO<Boolean> deleteLeave(Integer id) {
        int count = leaveService.deleteLeave(id);
        if (count > 0) {
            return ResultVO.success();
        } else {
            return ResultVO.fail();
        }
    }
}
