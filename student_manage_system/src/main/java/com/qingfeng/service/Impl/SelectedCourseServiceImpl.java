package com.qingfeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qingfeng.entity.Course;
import com.qingfeng.entity.SelectedCourse;
import com.qingfeng.mapper.CourseMapper;
import com.qingfeng.mapper.SelectedCourseMapper;
import com.qingfeng.service.SelectedCourseService;
import com.qingfeng.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 清风学Java
 */
@Service
public class SelectedCourseServiceImpl implements SelectedCourseService {

    private SelectedCourseMapper selectedCourseMapper;
    private CourseMapper courseMapper;

    @Autowired
    public SelectedCourseServiceImpl(SelectedCourseMapper selectedCourseMapper, CourseMapper courseMapper) {
        this.selectedCourseMapper = selectedCourseMapper;
        this.courseMapper = courseMapper;
    }

    /**
     *条件查询学生选课信息
     * @param paramMap
     * @return
     */
    @Override
    public PageBean<SelectedCourse> queryPage(Map<String, Object> paramMap) {
        PageBean<SelectedCourse> pageBean = new PageBean<>((Integer) paramMap.get("pageno"), (Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex", startIndex);
        //查询学生选课信息
        List<SelectedCourse> datas = selectedCourseMapper.queryList(paramMap);
        pageBean.setDatas(datas);

        //查询总记录数
        Integer totalsize = selectedCourseMapper.queryCount(paramMap);
        pageBean.setTotalsize(totalsize);
        return pageBean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addSelectedCourse(SelectedCourse selectedCourse) {
        //封装我们要添加的数据
        LambdaQueryWrapper<SelectedCourse> wrapper = new LambdaQueryWrapper<SelectedCourse>()
                .eq(SelectedCourse::getStudentId, selectedCourse.getStudentId())
                .eq(SelectedCourse::getCourseId, selectedCourse.getCourseId())
                .eq(SelectedCourse::getTeacherId,selectedCourse.getTeacherId());
        //查询是否有这条信息
        SelectedCourse selectOne = selectedCourseMapper.selectOne(wrapper);

        if (Objects.isNull(selectOne)) {
            //没有说明可以添加
            int count = courseMapper.addStudentNum(selectedCourse.getCourseId());
            if (count == 1) {
                selectedCourseMapper.insert(selectedCourse);
                return count;
            }
            if (count == 0) {
                return count;
            }
        } else {
            return 2;
        }
        return 3;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSelectedCourse(Integer selectedCourseId) {
        //删除前检查是否存在  健壮性处理
        SelectedCourse selectedCourse = selectedCourseMapper.selectById(selectedCourseId);
        if (Objects.isNull(selectedCourse)) {
            return -1;
        }
        // 退课
        return selectedCourseMapper.deleteById(selectedCourseId);
    }

    @Override
    public boolean checkSelectedCourse(Integer studentId) {
        LambdaQueryWrapper<SelectedCourse> wrapper = new LambdaQueryWrapper<SelectedCourse>()
                .eq(SelectedCourse::getStudentId, studentId);
        List<SelectedCourse> selectedCourseList = selectedCourseMapper.selectList(wrapper);
        //根据通过学生Id查询信息来判断是否是学生
        return selectedCourseList.isEmpty();
    }

    @Override
    public List<SelectedCourse> getAllBySid(Integer studentid) {
        LambdaQueryWrapper<SelectedCourse> wrapper = new LambdaQueryWrapper<SelectedCourse>()
                .eq(SelectedCourse::getStudentId, studentid);
        List<SelectedCourse> selectedCourses = selectedCourseMapper.selectList(wrapper);
        System.out.println(selectedCourses.toString());
        return selectedCourseMapper.getAllBySid(studentid);
    }

    @Override
    public Course getCourseDetail(Integer studentId, Integer courseId) {
        return selectedCourseMapper.getCourseDetail(studentId, courseId);
    }
}
