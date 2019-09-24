package com.ego.item.service;

import com.ego.common.pojo.PageResult;
import com.ego.item.mapper.BrandMapper;
import com.ego.item.pojo.Brand;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/9/24 22:44
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> getBrandByPage(Integer pageNo, Integer pageSize, String sortBy, Boolean descending, String key) {
        //todo
        PageHelper.startPage(pageNo,pageSize,descending);

        Page<Brand> page = (Page<Brand>)brandMapper.selectByExample("");
        return new PageResult<>(page.getTotal(),page);
    }
}
