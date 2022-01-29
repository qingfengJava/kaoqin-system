package com.qingfeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingfeng.entity.Attendance;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 清风学Java
 */
@Repository
public interface AttendanceMapper extends BaseMapper<Attendance> {

    /**
     * 分页条件查询学生考勤信息
     * @param paramMap
     * @return
     */
    List<Attendance> queryList(Map<String, Object> paramMap);

    /**
     * 查询总记录数
     * @param paramMap
     * @return
     */
    int queryCount(Map<String, Object> paramMap);

    /**
     * 根据Id进行补签
     * @param id
     * @return
     */
    int reissue(@Param("attendanceId") Integer id);

    /**
     * 在老师登录的情况下查询老师下的学生的考勤信息
     * @param paramMap
     * @return
     */
    List<Attendance> queryListByTeacherId(Map<String, Object> paramMap);

    /**
     * 在老师登录的情况下根据老师的条件查询总记录数
     * @param paramMap
     * @return
     */
    int queryCountByTeacherId(Map<String, Object> paramMap);

    /**
     * 修改考勤信息
     * @param attendance
     * @return
     */
    int updateAttendanceById(Attendance attendance);
}
