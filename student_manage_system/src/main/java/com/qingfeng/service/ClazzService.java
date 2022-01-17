package com.qingfeng.service;

import com.qingfeng.entity.Clazz;
import com.qingfeng.utils.PageBean;

import java.util.List;
import java.util.Map;

/**
 * 专业的业务层接口
 *
 * @author 清风学Javar
 */
public interface ClazzService {

    /**
     * 根据条件分页查询专业信息
     * @param paramMap
     * @return
     */
    PageBean<Clazz> queryPage(Map<String, Object> paramMap);

    /**
     * 根据专业id
     * @param ids
     * @return
     */
    int deleteClazz(List<Integer> ids);

    /**
     * 将新的信息修改
     * @param clazz
     * @return
     */
    int editClazz(Clazz clazz);

    /**
     * 添加专业
     * @param clazz
     * @return
     */
    int add(Clazz clazz);
}
