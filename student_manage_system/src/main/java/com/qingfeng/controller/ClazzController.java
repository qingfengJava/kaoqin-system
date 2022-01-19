package com.qingfeng.controller;

import com.qingfeng.constants.UserConstant;
import com.qingfeng.entity.Clazz;
import com.qingfeng.entity.User;
import com.qingfeng.service.ClazzService;
import com.qingfeng.service.UserService;
import com.qingfeng.utils.IdsData;
import com.qingfeng.utils.PageBean;
import com.qingfeng.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 清风学Java
 */
@Controller
@RequestMapping("/clazz")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    @Autowired
    private UserService userService;

    /**
     * 跳转专业页面
     */
    @GetMapping("/clazz_list")
    public String clazzList() {
        return "/clazz/clazzList";
    }

    /**
     * 异步加载专业列表
     * @param page 页码  默认是1
     * @param rows  每页显示的条目数  默认是10
     * @param clazzName 专业名称
     * @param from
     * @return
     */
    @PostMapping("/getClazzList")
    @ResponseBody
    public Object getClazzList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "rows", defaultValue = "10") Integer rows, String clazzName, String from, HttpSession session) {
        //map封装的是我们要查询的条件信息
        Map<String, Object> paramMap = new HashMap<>(10);
        paramMap.put("pageno", page);
        paramMap.put("pagesize", rows);
        //判断是否有专业名称，有就是按条件搜索，没有就是全局搜索
        if (!StringUtils.isEmpty(clazzName)) {
            paramMap.put("name", clazzName);
        }
        //判断是不是教师，是教师的话只搜索当前教师下的专业
        //获取session中的user对象
        User loginUser = (User) session.getAttribute(UserConstant.LOGIN_USER);
        if (UserConstant.TEACHER_CODE.equals(loginUser.getUserType()) || UserConstant.STUDENT_CODE.equals(loginUser.getUserType())){
            paramMap.put("id",loginUser.getClassId());
        }

        //根据条件分页查询
        PageBean<Clazz> pageBean = clazzService.queryPage(paramMap);
        //如果是combox直接返回
        if (!StringUtils.isEmpty(from) && "combox".equals(from)) {
            return pageBean.getDatas();
        } else {
            Map<String, Object> result = new HashMap<>(10);
            result.put("total", pageBean.getTotalsize());
            result.put("rows", pageBean.getDatas());
            return result;
        }
    }

    /**
     * 添加专业
     */
    @PostMapping("/addClazz")
    @ResponseBody
    public ResultVO<Boolean> addClazz(Clazz clazz) {
        int count = clazzService.add(clazz);
        if (count > 0) {
            return ResultVO.success();
        } else {
            return ResultVO.fail();
        }
    }

    /**
     * 删除专业
     */
    @PostMapping("/deleteClazz")
    @ResponseBody
    public ResultVO<Boolean> deleteClazz(IdsData data) {
        //获取专业Id的集合
        List<Integer> ids = data.getIds();
        // 判断是否存在课程关联学生
        for (Integer id : ids) {
            //根据Id查询是否有学生选择这个专业
            if (!userService.checkStudentInCourse(id)) {
                return ResultVO.fail("无法删除，专业下存在老师或者学生");
            }
        }

        //根据Id进行删除，返回的是影响的行数
        int count = clazzService.deleteClazz(data.getIds());
        if (count > 0) {
            return ResultVO.success();
        } else {
            return ResultVO.fail();
        }
    }

    /**
     * 专业修改
     */
    @PostMapping("/editClazz")
    @ResponseBody
    public ResultVO<Boolean> editClazz(Clazz clazz) {
        int count = clazzService.editClazz(clazz);
        if (count > 0) {
            return ResultVO.success();
        } else {
            return ResultVO.fail();
        }
    }
}
