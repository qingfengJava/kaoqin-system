package com.qingfeng.service;

import com.qingfeng.entity.Course;
import com.qingfeng.utils.PageBean;

import java.util.List;
import java.util.Map;

/**
 * @author 清风学Java
 */
public interface CourseService {
    /**
     * 分页查询课程数据
     * @param paramMap
     * @return
     */
    PageBean<Course> queryPage(Map<String, Object> paramMap);

    /**
     * 添加课程信息
     * @param course
     * @param userType
     * @return
     */
    int addCourse(Course course,String userType);

    /**
     * 更新课程信息
     * @param course
     * @param userType
     * @return
     */
    int editCourse(Course course,String userType);

    /**
     * 根据Id删除课程
     * @param ids
     * @return
     */
    int deleteCourse(List<Integer> ids);

    /**
     * 查询课程Id
     * @param ids
     * @param teacherId
     * @return
     */
    List<Course> getCourseById(List<Integer> ids, int teacherId);

    /**
     * 根据Id查询课程
     * @param ids
     * @return
     */
    List<Course> getCourseById(List<Integer> ids);
}
