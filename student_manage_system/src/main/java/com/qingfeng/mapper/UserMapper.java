package com.qingfeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingfeng.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 清风学Java
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    List<User> queryList(Map<String, Object> paramMap);

    /**
     * 查询总记录数
     * @param paramMap
     * @return
     */
    Integer queryCount(Map<String, Object> paramMap);

    /**
     * 条件查询学生信息
     * @param paramMap
     * @return
     */
    List<User> getStudentList(Map<String, Object> paramMap);

    List<User> getTeacherList(Map<String, Object> paramMap);
}
