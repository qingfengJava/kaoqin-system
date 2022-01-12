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

    List<Leave> queryList(Map<String, Object> paramMap);

    Integer queryCount(Map<String, Object> paramMap);

    int addLeave(Leave leave);

    int editLeave(Leave leave);

    int checkLeave(Leave leave);

    int deleteLeave(Integer id);
}
