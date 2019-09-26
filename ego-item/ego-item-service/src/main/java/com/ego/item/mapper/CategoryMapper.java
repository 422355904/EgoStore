package com.ego.item.mapper;

import com.ego.item.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/9/24 15:33
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
public interface CategoryMapper extends Mapper<Category>{

    @Select("select * from tb_category where id in (select category_id from tb_category_brand where brand_id=#{bid})")
    List<Category> findCategoryByBid(@Param("bid")Long bid);
}
