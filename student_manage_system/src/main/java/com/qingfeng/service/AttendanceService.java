package com.qingfeng.service;


import com.qingfeng.entity.Attendance;
import com.qingfeng.utils.PageBean;

import java.util.List;
import java.util.Map;

/**
 * @author 清风学Java
 */
public interface AttendanceService {
    PageBean<Attendance> queryPage(Map<String, Object> paramMap);

    boolean checkAttendance(Attendance attendance);

    int addAttendance(Attendance attendance);

    int reissue(Integer id);

    List<Attendance> selectList();

    int deleteList(List<Attendance> attendanceList);
}
