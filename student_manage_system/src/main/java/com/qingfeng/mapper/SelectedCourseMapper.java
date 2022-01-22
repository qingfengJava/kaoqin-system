package com.qingfeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingfeng.entity.Course;
import com.qingfeng.entity.SelectedCourse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 清风学Java
 */
@Repository
public interface SelectedCourseMapper extends BaseMapper<SelectedCourse> {
    /**
     * 条件查询学生选课信息
     * @param paramMap
     * @return
     */
    List<SelectedCourse> queryList(Map<String, Object> paramMap);

    /**
     * 条件查询总记录数
     * @param paramMap
     * @return
     */
    int queryCount(Map<String, Object> paramMap);

    int addSelectedCourse(SelectedCourse selectedCourse);

    SelectedCourse findBySelectedCourse(SelectedCourse selectedCourse);

    SelectedCourse findById(Integer id);

    int deleteSelectedCourse(Integer id);

    List<SelectedCourse> isStudentId(Integer id);

    List<SelectedCourse> getAllBySid(Integer studentid);

    Course getCourseDetail(@Param("studentId") Integer studentId, @Param("courseId") Integer courseId);
}
