package com.ego.cart.pojo;

import lombok.Data;

/**
 * @Author TheKing
 * @Date 2019/10/12 16:44
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Data
public class Cart {
    private Long userId;// 用户id
    private Long skuId;// 商品id
    private String title;// 标题
    private String image;// 图片
    private Long price;// 加入购物车时的价格
    private Integer num;// 购买数量
    private String ownSpec;// 商品规格参数
}
