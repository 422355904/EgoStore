package com.ego.item.service;

import com.ego.common.pojo.PageResult;
import com.ego.item.mapper.PromotionMapper;
import com.ego.item.pojo.Promotion;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author TheKing
 * @Date 2019/10/18 13:57
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Service
public class PromotionService {

    @Autowired
    private PromotionMapper promotionMapper;

    /**
     * 促销分页
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param descending
     * @param key
     * @return
     */
    public PageResult<Promotion> getPromotionByPage(Integer pageNo, Integer pageSize, String sortBy, Boolean descending, String key) {
        //分页
        PageHelper.startPage(pageNo,pageSize);
        //查询条件
        Example example=new Example(Promotion.class);
        Example.Criteria criteria = example.createCriteria();
        //排序条件
        if (StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy+" "+(descending?"desc":"asc"));
        }
        //关键字条件
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("description","%"+key+"%");
        }

        //查询结果
        Page<Promotion> pageInfo= (Page<Promotion>)promotionMapper.selectByExample(example);

        return new PageResult<Promotion>(pageInfo.getTotal(),pageInfo.getResult());
    }

    @Transactional
    public void deletePromotion(Long id) {
        promotionMapper.deleteByPrimaryKey(id);
    }
}
