package com.ego.item.mapper;

import com.ego.item.pojo.Sku;
import com.ego.item.pojo.Spu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/9/27 13:59
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
public interface SkuMapper extends Mapper<Sku>, SelectByIdListMapper<Sku,Long> {

}
