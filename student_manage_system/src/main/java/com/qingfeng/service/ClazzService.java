package com.qingfeng.service;

import com.qingfeng.entity.Clazz;
import com.qingfeng.utils.PageBean;

import java.util.List;
import java.util.Map;

/**
 * @author 清风学Javar
 */
public interface ClazzService {

    PageBean<Clazz> queryPage(Map<String, Object> paramMap);

    int deleteClazz(List<Integer> ids);

    int editClazz(Clazz clazz);

    int add(Clazz clazz);
}
