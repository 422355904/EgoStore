package com.ego.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author TheKing
 * @Date 2019/9/27 14:16
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
//@Data
@Table(name = "tb_stock")
public class Stock {
    @Id
    private Long skuId;
    private Integer seckillStock;// 秒杀可用库存
    private Integer seckillTotal;// 已秒杀数量
    private Integer stock;// 正常库存

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getSeckillStock() {
        return seckillStock;
    }

    public void setSeckillStock(Integer seckillStock) {
        this.seckillStock = seckillStock;
    }

    public Integer getSeckillTotal() {
        return seckillTotal;
    }

    public void setSeckillTotal(Integer seckillTotal) {
        this.seckillTotal = seckillTotal;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
