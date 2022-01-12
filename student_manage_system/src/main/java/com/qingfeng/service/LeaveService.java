package com.qingfeng.service;


import com.qingfeng.entity.Leave;
import com.qingfeng.utils.PageBean;

import java.util.Map;

/**
 * @author 清风学Java
 */
public interface LeaveService {
    PageBean<Leave> queryPage(Map<String, Object> paramMap);

    int addLeave(Leave leave);

    int editLeave(Leave leave);

    int checkLeave(Leave leave);

    int deleteLeave(Integer id);
}
