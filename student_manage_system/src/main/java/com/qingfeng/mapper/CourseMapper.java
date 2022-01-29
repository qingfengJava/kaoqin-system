package com.qingfeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingfeng.entity.Course;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 添加选课人数
     * @param courseId
     * @return
     */
    int addStudentNum(Integer courseId);

    /**
     * 删除选课人数
     * @param courseId
     */
    void deleteStudentNum(Integer courseId);

    /**
     * 查询课程
     * @param ids
     * @return
     */
    List<Course> getCourseById(List<Integer> ids);

    /**
     * 查询教师下学生选择的课程信息
     * @param ids
     * @param teacherId
     * @return
     */
    List<Course> getCourseByTeacherId(@Param("ids") List<Integer> ids,@Param("teacherId") int teacherId);

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

}
