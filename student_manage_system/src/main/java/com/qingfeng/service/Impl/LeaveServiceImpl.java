package com.qingfeng.service.Impl;


import com.qingfeng.entity.Leave;
import com.qingfeng.mapper.LeaveMapper;
import com.qingfeng.service.LeaveService;
import com.qingfeng.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 清风学Java
 */
@Service
public class LeaveServiceImpl implements LeaveService {

    private LeaveMapper leaveMapper;

    @Autowired
    public LeaveServiceImpl(LeaveMapper leaveMapper) {
        this.leaveMapper = leaveMapper;
    }

    /**
     * 查询请假信息
     * @param paramMap
     * @return
     */
    @Override
    public PageBean<Leave> queryPage(Map<String, Object> paramMap) {
        PageBean<Leave> pageBean = new PageBean<>((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex",startIndex);

        //定义用于封装数据的变量
        List<Leave> datas = null;
        Integer totalSize = 0;

        if (paramMap.get("teacherId") != null){
            //是老师只加载老师下的学生的请假信息
            datas = leaveMapper.queryListByTeacherId(paramMap);
            totalSize = leaveMapper.queryCountByTeacherId(paramMap);
        }else{
            //不是老师，按照条件查询
            datas = leaveMapper.queryList(paramMap);
            totalSize = leaveMapper.queryCount(paramMap);
        }

        pageBean.setDatas(datas);
        pageBean.setTotalsize(totalSize);
        return pageBean;
    }

    /**
     * 添加请假信息
     * @param leave
     * @return
     */
    @Override
    public int addLeave(Leave leave) {
        return leaveMapper.addLeave(leave);
    }

    @Override
    public int editLeave(Leave leave) {
        return leaveMapper.editLeave(leave);
    }

    @Override
    public int checkLeave(Leave leave) {
        return leaveMapper.checkLeave(leave);
    }

    @Override
    public int deleteLeave(Integer id) {
        return leaveMapper.deleteLeave(id);
    }
}
