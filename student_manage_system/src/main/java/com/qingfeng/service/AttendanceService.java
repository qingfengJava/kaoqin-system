package com.qingfeng.service;


import com.qingfeng.entity.Attendance;
import com.qingfeng.utils.PageBean;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author 清风学Java
 */
public interface AttendanceService {
    /**
     * 分页条件查询学生的考勤信息
     * @param paramMap
     * @return
     */
    PageBean<Attendance> queryPage(Map<String, Object> paramMap);

    /**
     * 检查考勤签到
     * @param attendance
     * @return
     * @throws ParseException
     */
    boolean checkAttendance(Attendance attendance) throws ParseException;

    /**
     * 添加考勤签到
     * @param attendance
     * @return
     */
    int addAttendance(Attendance attendance);

    /**
     * 根据Id补签
     * @param id
     * @return
     */
    int reissue(Integer id);

    /**
     * 查询考勤信息
     * @return
     */
    List<Attendance> selectList();

    /**
     * 删除考勤信息
     * @param ids
     * @return
     */
    int deleteList(List<Integer> ids);

    /**
     * 修改考勤信息
     * @param attendance
     * @return
     */
    int updateAttendance(Attendance attendance);
}
