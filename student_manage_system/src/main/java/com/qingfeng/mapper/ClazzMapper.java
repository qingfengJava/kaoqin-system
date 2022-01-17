package com.qingfeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qingfeng.entity.Clazz;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 清风学Java
 */
@Repository
public interface ClazzMapper extends BaseMapper<Clazz> {

    /**
     * 根据条件查询专业信息
     * @param paramMap
     * @return
     */
    List<Clazz> queryList(Map<String, Object> paramMap);

    /**
     * 查询专业的总记录数
     * @param paramMap
     * @return
     */
    Integer queryCount(Map<String, Object> paramMap);
}
