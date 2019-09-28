package com.ego.item.pojo;

import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/9/27 15:57
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Data
public class SpuBo extends Spu {
    @Transient
    private String categoryName;
    @Transient
    private String brandName;
    @Transient
    SpuDetail spuDetail;// 商品详情
    @Transient
    List<Sku> skus;// sku列表


}
