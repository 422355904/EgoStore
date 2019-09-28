package com.ego.item.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author TheKing
 * @Date 2019/9/27 14:16
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Data
@Table(name = "tb_sku")
public class Sku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long spuId;
    private String title;
    private String images;
    private Long price;
    private String ownSpec;// 商品特殊规格的键值对
    private String indexes;// 商品特殊规格的下标
    private Boolean enable;// 是否有效，逻辑删除用
    private Date createTime;// 创建时间
    private Date lastUpdateTime;// 最后修改时间
    @Transient
    private Stock stock;// 库存

}
