package com.qingfeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingfeng.entity.Leave;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 清风学Java
 */
@Repository
public interface LeaveMapper extends BaseMapper<Leave> {

    /**
     * 查询请假信息
     * @param paramMap
     * @return
     */
    List<Leave> queryList(Map<String, Object> paramMap);

    Integer queryCount(Map<String, Object> paramMap);

    /**
     * 添加请假信息
     * @param leave
     * @return
     */
    int addLeave(Leave leave);

    int editLeave(Leave leave);

    /**
     * 审核请假信息
     * @param leave
     * @return
     */
    int checkLeave(Leave leave);

    int deleteLeave(Integer id);

    /**
     * 查询教师下学生的请假信息
     * @param paramMap
     * @return
     */
    List<Leave> queryListByTeacherId(Map<String, Object> paramMap);

    /**
     * 查询老师下学生请假的总记录数
     * @param paramMap
     * @return
     */
    Integer queryCountByTeacherId(Map<String, Object> paramMap);
}
