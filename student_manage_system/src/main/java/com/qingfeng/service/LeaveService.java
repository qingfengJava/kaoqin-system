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

    /**
     * 修改请假信息
     * @param leave
     * @return
     */
    int editLeave(Leave leave);

    /**
     * 审核请假信息
     * @param leave
     * @return
     */
    int checkLeave(Leave leave);

    /**
     * 删除请假信息
     * @param id
     * @return
     */
    int deleteLeave(Integer id);
}
