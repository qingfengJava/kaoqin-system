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

    List<Attendance> queryList(Map<String, Object> paramMap);

    int queryCount(Map<String, Object> paramMap);

    int reissue(@Param("attendanceId") Integer id);
}
