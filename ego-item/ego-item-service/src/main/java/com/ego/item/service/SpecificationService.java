package com.ego.item.service;

import com.ego.item.mapper.SpecificationMapper;
import com.ego.item.pojo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author TheKing
 * @Date 2019/9/27 13:58
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Service
public class SpecificationService {

    @Autowired
    private SpecificationMapper specMapper;

    public Specification queryByCid(Long cid) {
        return specMapper.selectByPrimaryKey(cid);
    }

    //新增种类规格模板
    public void saveSpecification(Specification specification) {
        specMapper.insertSelective(specification);
    }
}
