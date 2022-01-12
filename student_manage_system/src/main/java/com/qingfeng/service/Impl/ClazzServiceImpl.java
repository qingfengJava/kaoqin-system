package com.qingfeng.service.Impl;

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
 * @author 清风学Java
 */
@Service
public class ClazzServiceImpl implements ClazzService {

    private final ClazzMapper clazzMapper;

    @Autowired
    public ClazzServiceImpl(ClazzMapper clazzMapper) {
        this.clazzMapper = clazzMapper;
    }

    @Override
    public PageBean<Clazz> queryPage(Map<String, Object> paramMap) {
        PageBean<Clazz> pageBean = new PageBean<>((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex",startIndex);
        List<Clazz> datas = clazzMapper.queryList(paramMap);
        pageBean.setDatas(datas);

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
        return clazzMapper.updateById(clazz);
    }

    @Override
    public int add(Clazz clazz) {
        return clazzMapper.insert(clazz);
    }

}
