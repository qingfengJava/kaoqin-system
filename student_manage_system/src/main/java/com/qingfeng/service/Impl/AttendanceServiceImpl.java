package com.qingfeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qingfeng.entity.Attendance;
import com.qingfeng.entity.Course;
import com.qingfeng.mapper.AttendanceMapper;
import com.qingfeng.mapper.CourseMapper;
import com.qingfeng.service.AttendanceService;
import com.qingfeng.utils.GetDayForWeek;
import com.qingfeng.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 清风学Java
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceMapper attendanceMapper;
    private final CourseMapper courseMapper;

    @Autowired
    public AttendanceServiceImpl(AttendanceMapper attendanceMapper, CourseMapper courseMapper) {
        this.attendanceMapper = attendanceMapper;
        this.courseMapper = courseMapper;
    }

    @Override
    public PageBean<Attendance> queryPage(Map<String, Object> paramMap) {
        PageBean<Attendance> pageBean = new PageBean<>(
                (Integer) paramMap.get("pageno"),
                (Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex", startIndex);
        //根据信息进行分页查询
        if ("".equals(paramMap.get("teacherId"))){
            List<Attendance> attendanceList = attendanceMapper.queryList(paramMap);
            pageBean.setDatas(attendanceList);
            Integer totalSize = attendanceMapper.queryCount(paramMap);
            pageBean.setTotalsize(totalSize);
        }else{
            //有老师的情况下
            List<Attendance> attendanceList = attendanceMapper.queryListByTeacherId(paramMap);
            pageBean.setDatas(attendanceList);
            Integer totalSize = attendanceMapper.queryCountByTeacherId(paramMap);
            pageBean.setTotalsize(totalSize);
        }



        return pageBean;
    }

    /**
     * 学生签到，规定学生只能是该课程当天签到，而且只能在上课时间段内签到才是有效，否则是迟到或缺勤
     * @param attendance
     * @return
     */
    @Override
    public boolean checkAttendance(Attendance attendance){
        //添加考勤签到，我们要判断签到的这门课是否存在，是否是重复签到的一天
        //根据签到的当天日期判断当天有没有这个课
        String weekDay = GetDayForWeek.getDateDayForWeek(attendance.getCourseDate());
        if (weekDay.equals(attendance.getCourseWeak())){
            //根据课程名称以及星期几查询该课程是否存在
            LambdaQueryWrapper<Course> wrapper1 = new LambdaQueryWrapper<Course>()
                    .eq(Course::getId, attendance.getCourseId())
                    .eq(Course::getWeakday, attendance.getCourseWeak());
            Course course = courseMapper.selectOne(wrapper1);
            if (course != null){
                //说明课程存在，继续判断是否已经签到
                LambdaQueryWrapper<Attendance> wrapper2 = new LambdaQueryWrapper<Attendance>()
                        .eq(Attendance::getCourseId, attendance.getCourseId())
                        .eq(Attendance::getStudentId, attendance.getStudentId())
                        .eq(Attendance::getCourseDate, attendance.getCourseDate())
                        .last("LIMIT 1");
                return Objects.nonNull(attendanceMapper.selectOne(wrapper2));
            }
        }

        return true;
    }

    /**
     * 考勤签到
     * @param attendance
     * @return
     */
    @Override
    public int addAttendance(Attendance attendance) {
        return attendanceMapper.insert(attendance);
    }

    /**
     * 补签
     * @param id
     * @return
     */
    @Override
    public int reissue(Integer id) {
        return attendanceMapper.reissue(id);
    }

    /**
     * 查询考勤信息
     * @return
     */
    @Override
    public List<Attendance> selectList() {
        return attendanceMapper.selectList(null);
    }

    /**
     * 删除考勤信息
     * @param ids
     * @return
     */
    @Override
    public int deleteList(List<Integer> ids) {
        return attendanceMapper.deleteBatchIds(ids);
    }

    /**
     * 修改考勤信息
     * @param attendance
     * @return
     */
    @Override
    public int updateAttendance(Attendance attendance) {
        return attendanceMapper.updateAttendanceById(attendance);
    }
}
