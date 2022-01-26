package com.qingfeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingfeng.entity.Course;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 清风学Java
 */
@Repository
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 分页条件查询课程信息
     * @param paramMap
     * @return
     */
    List<Course> queryList(Map<String, Object> paramMap);

    /**
     * 查询课程总记录数
     * @param paramMap
     * @return
     */
    Integer queryCount(Map<String, Object> paramMap);

    int addStudentNum(Integer courseId);

    void deleteStudentNum(Integer courseId);

    List<Course> getCourseById(List<Integer> ids);

    /**
     * 条件查询课程信息
     * @param paramMap
     * @return
     */
    Course queryCourse(Map<String, Object> paramMap);

    /**
     * 删除学生选课的选课信息
     * @param id
     */
    void deleteByCourseId(Integer id);

    /**
     * 根据学生Id查询学生选择的课程
     * @param paramMap
     * @return
     */
    List<Course> queryListByStudentId(Map<String, Object> paramMap);

    /**
     * 根据学生Id查询学生选课的总记录数
     * @param paramMap
     * @return
     */
    Integer queryCountByStudentId(Map<String, Object> paramMap);

    /**
     * 根据教师Id查询教师下的课程
     * @param paramMap
     * @return
     */
    List<Course> queryListByTeacherId(Map<String, Object> paramMap);

    /**
     * 查询教师下课程的总记录数
     * @param paramMap
     * @return
     */
    Integer queryStuCountByTeacherId(Map<String, Object> paramMap);
}
