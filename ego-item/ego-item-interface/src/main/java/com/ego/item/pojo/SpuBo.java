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
//@Data
public class SpuBo extends Spu {
    @Transient
    private String categoryNames; //spuId对应分类名字
    @Transient
    private String brandName; //品牌名字
    @Transient
    private SpuDetail spuDetail;// 商品详情
    @Transient
    private List<Sku> skus;// sku列表

    public String getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(String categoryNames) {
        this.categoryNames = categoryNames;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public SpuDetail getSpuDetail() {
        return spuDetail;
    }

    public void setSpuDetail(SpuDetail spuDetail) {
        this.spuDetail = spuDetail;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }
}
