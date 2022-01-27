package com.qingfeng.service;


import com.qingfeng.entity.Leave;
import com.qingfeng.utils.PageBean;

import java.util.Map;

/**
 * @author 清风学Java
 */
public interface LeaveService {
    /**
     * 查询请假信息
     * @param paramMap
     * @return
     */
    PageBean<Leave> queryPage(Map<String, Object> paramMap);

    /**
     * 添加请假信息
     * @param leave
     * @return
     */
    int addLeave(Leave leave);

    int editLeave(Leave leave);

    int checkLeave(Leave leave);

    int deleteLeave(Integer id);
}
