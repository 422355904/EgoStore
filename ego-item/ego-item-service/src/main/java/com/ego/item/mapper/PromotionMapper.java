package com.ego.item.mapper;

import com.ego.item.pojo.Promotion;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author TheKing
 * @Date 2019/10/18 14:05
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
public interface PromotionMapper extends Mapper<Promotion>, SelectByIdListMapper<Promotion,Long> {
}
