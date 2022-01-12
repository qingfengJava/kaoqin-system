package com.qingfeng.service;


import com.qingfeng.entity.Course;
import com.qingfeng.entity.SelectedCourse;
import com.qingfeng.utils.PageBean;

import java.util.List;
import java.util.Map;

/**
 * @author 清风学Java
 */
public interface SelectedCourseService {
    PageBean<SelectedCourse> queryPage(Map<String, Object> paramMap);

    int addSelectedCourse(SelectedCourse selectedCourse);

    int deleteSelectedCourse(Integer id);

    boolean checkSelectedCourse(Integer studentId);

    List<SelectedCourse> getAllBySid(Integer studentId);

    Course getCourseDetail(Integer studentId, Integer courseId);
}
