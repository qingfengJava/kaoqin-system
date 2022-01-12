package com.qingfeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qingfeng.entity.Attendance;
import com.qingfeng.mapper.AttendanceMapper;
import com.qingfeng.service.AttendanceService;
import com.qingfeng.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 清风学Java
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceMapper attendanceMapper;

    @Autowired
    public AttendanceServiceImpl(AttendanceMapper attendanceMapper) {
        this.attendanceMapper = attendanceMapper;
    }

    @Override
    public PageBean<Attendance> queryPage(Map<String, Object> paramMap) {
        PageBean<Attendance> pageBean = new PageBean<>(
                (Integer) paramMap.get("pageno"),
                (Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex", startIndex);
        List<Attendance> attendanceList = attendanceMapper.queryList(paramMap);
        pageBean.setDatas(attendanceList);

        Integer totalSize = attendanceMapper.queryCount(paramMap);
        pageBean.setTotalsize(totalSize);
        return pageBean;
    }

    @Override
    public boolean checkAttendance(Attendance attendance) {
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<Attendance>()
                .eq(Attendance::getCourseId, attendance.getCourseId())
                .eq(Attendance::getStudentId, attendance.getStudentId())
                .last("LIMIT 1");
        return Objects.nonNull(attendanceMapper.selectOne(wrapper));
    }

    @Override
    public int addAttendance(Attendance attendance) {
        return attendanceMapper.insert(attendance);
    }

    @Override
    public int reissue(Integer id) {
        return attendanceMapper.reissue(id);
    }

    @Override
    public List<Attendance> selectList() {
        return attendanceMapper.selectList(null);
    }

    @Override
    public int deleteList(List<Attendance> attendanceList) {
        List<Integer> collect = attendanceList.stream().map(Attendance::getId).collect(Collectors.toList());
        return attendanceMapper.deleteBatchIds(collect);
    }
}
