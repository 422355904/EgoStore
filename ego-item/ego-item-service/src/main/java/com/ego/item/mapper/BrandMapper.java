package com.ego.item.mapper;

import com.ego.item.pojo.Brand;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/9/24 23:22
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
public interface BrandMapper extends Mapper<Brand> {

    @Insert("insert into tb_category_brand(category_id,brand_id) values(#{cid},#{bid})")
    void saveBrandCategory(@Param("cid") Long cid, @Param("bid") Long id);

    @Delete("delete from tb_category_brand where brand_id=#{bid}")
    void deleteBrandCategory(@Param("bid") Long bid);

    @Select("select b.* from tb_brand b inner join tb_category_brand cb on b.id=cb.brand_id where cb.category_id=#{cid}")
    List<Brand> getGoodsBrandByCid(@Param("cid")Long cid);

    /**
     * Invalid bound statement (not found): com.ego.item.mapper.BrandMapper.saveBrandCategory
     */
}
