package com.qingfeng.service.Impl;

import com.qingfeng.entity.Course;
import com.qingfeng.mapper.CourseMapper;
import com.qingfeng.service.CourseService;
import com.qingfeng.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 清风学Java
 */
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;

    @Autowired
    public CourseServiceImpl(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }


    @Override
    public PageBean<Course> queryPage(Map<String, Object> paramMap) {
        PageBean<Course> pageBean = new PageBean<>((Integer) paramMap.get("pageno"), (Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex", startIndex);
        //分页查询数据
        List<Course> datas = courseMapper.queryList(paramMap);
        pageBean.setDatas(datas);
        //查询总记录数
        Integer totalsize = courseMapper.queryCount(paramMap);
        pageBean.setTotalsize(totalsize);
        return pageBean;
    }

    @Override
    public int addCourse(Course course,String userType) {
        //添加课程信息之前，要检查课程时间是否冲突，但是管理员可以强行添加课程
        Map<String, Object> paramMap;
        if (!"1".equals(userType)){
            //只要不是管理员，就必须校验  根据课程名，上课星期，上课时间进行查询
            paramMap = new HashMap<>(10);
            paramMap.put("name",course.getName());
            Course course1 =  courseMapper.queryCourse(paramMap);
            if (course1 == null ){
                //说明课程名不冲突，检查上课时间
                paramMap = new HashMap<>(10);
                paramMap.put("courseDate",course.getCourseDate());
                paramMap.put("weakday",course.getWeakday());
                Course course2 =  courseMapper.queryCourse(paramMap);
                if (course2 == null){
                    return courseMapper.insert(course);
                }
            }
            //否则课程冲突，添加失败
            return -1;
        }else{
            return courseMapper.insert(course);
        }
    }

    @Override
    public int editCourse(Course course,String userType) {
        //添加课程信息之前，要检查课程时间是否冲突，但是管理员可以强行添加课程
        Map<String, Object> paramMap;
        if (!"1".equals(userType)){
            //只要不是管理员，就必须校验  根据课程名，上课星期，上课时间进行查询
            paramMap = new HashMap<>(10);
            paramMap.put("name",course.getName());
            Course course1 =  courseMapper.queryCourse(paramMap);
            if (course1 == null ){
                //说明课程名不冲突，检查上课时间
                paramMap = new HashMap<>(10);
                paramMap.put("courseDate",course.getCourseDate());
                paramMap.put("weakday",course.getWeakday());
                Course course2 =  courseMapper.queryCourse(paramMap);
                if (course2 == null){
                    return courseMapper.updateById(course);
                }
            }
            //否则课程冲突，添加失败
            return -1;
        }else{
            return courseMapper.updateById(course);
        }

    }

    @Override
    public int deleteCourse(List<Integer> ids) {
        //删除的时候，要顺带把选了这个课的学生的选课信息一起删除
        for (Integer id : ids) {
            courseMapper.deleteByCourseId(id);
        }
        return courseMapper.deleteBatchIds(ids);
    }

    @Override
    public List<Course> getCourseById(List<Integer> ids) {
        return courseMapper.getCourseById(ids);
    }
}
