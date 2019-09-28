package com.ego.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author TheKing
 * @Date 2019/9/27 14:15
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Data
@Table(name="tb_spu_detail")
public class SpuDetail {
    @Id
    private Long spuId;// 对应的SPU的id
    private String description;// 商品描述
    private String specTemplate;// 商品特殊规格的名称及可选值模板
    private String specifications;// 商品的全局规格属性
    private String packingList;// 包装清单
    private String afterService;// 售后服务

}