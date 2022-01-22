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

    /**
     * 条件查询学生选课信息
     * @param paramMap
     * @return
     */
    PageBean<SelectedCourse> queryPage(Map<String, Object> paramMap);

    /**
     * 添加学生选课信息
     * @param selectedCourse
     * @return
     */
    int addSelectedCourse(SelectedCourse selectedCourse);

    /**
     * 根据Id，删除选课信息
     * @param id
     * @return
     */
    int deleteSelectedCourse(Integer id);

    /**
     * 检查学生信息
     * @param studentId
     * @return
     */
    boolean checkSelectedCourse(Integer studentId);

    List<SelectedCourse> getAllBySid(Integer studentId);

    Course getCourseDetail(Integer studentId, Integer courseId);
}
