package com.qingfeng.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qingfeng.entity.Clazz;
import com.qingfeng.mapper.ClazzMapper;
import com.qingfeng.service.ClazzService;
import com.qingfeng.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 专业的业务层接口实现
 *
 * @author 清风学Java
 */
@Service
public class ClazzServiceImpl implements ClazzService {

    private final ClazzMapper clazzMapper;

    @Autowired
    public ClazzServiceImpl(ClazzMapper clazzMapper) {
        this.clazzMapper = clazzMapper;
    }

    /**
     * 根据条件分页查询专业信息
     * @param paramMap
     * @return
     */
    @Override
    public PageBean<Clazz> queryPage(Map<String, Object> paramMap) {
        //先将map中的信息封装到pageBean对象中
        PageBean<Clazz> pageBean = new PageBean<>((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex",startIndex);
        //查询专业信息
        List<Clazz> datas = clazzMapper.queryList(paramMap);
        pageBean.setDatas(datas);
        //查询总记录数
        Integer totalsize = clazzMapper.queryCount(paramMap);
        pageBean.setTotalsize(totalsize);
        return pageBean;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteClazz(List<Integer> ids) {
        return clazzMapper.deleteBatchIds(ids);
    }

    @Override
    public int editClazz(Clazz clazz) {
        //修改是更具专业名字进行修改的
        return clazzMapper.updateById(clazz);
    }

    @Override
    public int add(Clazz clazz) {
        //添加之前要检查改专业是否已经存在
        LambdaQueryWrapper<Clazz> wrapper = new LambdaQueryWrapper<Clazz>()
                .eq(Clazz::getName, clazz.getName());
        List<Clazz> clazz2 = clazzMapper.selectList(wrapper);
        if (clazz2.size() == 0){
            //如果等于null，说明不存在可以添加
            return clazzMapper.insert(clazz);
        }

        //否者返回-1
        return -1;
    }

}
