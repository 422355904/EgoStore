package com.ego.search.dto;

import com.ego.common.pojo.PageResult;
import com.ego.item.pojo.Brand;
import com.ego.item.pojo.Category;
import com.ego.search.pojo.Goods;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author TheKing
 * @Date 2019/10/3 13:38
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Data
public class SearchResponse extends PageResult<Goods> {

    private List<Category> categories;// 分类过滤条件

    private List<Brand> brands;// 品牌过滤条件

    private List<Map<String,Object>> specs; // 规格参数过滤条件

    public SearchResponse() {
    }

    public SearchResponse(Long total, Long totalPage, List<Goods> items, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}
