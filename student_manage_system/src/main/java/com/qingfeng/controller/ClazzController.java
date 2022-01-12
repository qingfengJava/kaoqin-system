package com.qingfeng.controller;

import com.qingfeng.entity.Clazz;
import com.qingfeng.service.ClazzService;
import com.qingfeng.service.UserService;
import com.qingfeng.utils.IdsData;
import com.qingfeng.utils.PageBean;
import com.qingfeng.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
     */
    @PostMapping("/getClazzList")
    @ResponseBody
    public Object getClazzList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "rows", defaultValue = "100") Integer rows, String clazzName, String from) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("pageno", page);
        paramMap.put("pagesize", rows);
        if (!StringUtils.isEmpty(clazzName)) {
            paramMap.put("name", clazzName);
        }
        PageBean<Clazz> pageBean = clazzService.queryPage(paramMap);
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
        List<Integer> ids = data.getIds();
        // 判断是否存在课程关联学生
        for (Integer id : ids) {
            if (!userService.checkStudentInCourse(id)) {
                return ResultVO.fail("无法删除，专业下存在学生");
            }
        }
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
