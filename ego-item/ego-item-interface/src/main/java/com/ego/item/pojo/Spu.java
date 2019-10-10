package com.ego.item.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author TheKing
 * @Date 2019/9/27 14:14
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Data
@Table(name = "tb_spu")
public class Spu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long brandId;
    private Long cid1;// 1级类目
    private Long cid2;// 2级类目
    private Long cid3;// 3级类目
    private String title;// 标题
    private String subTitle;// 子标题
    private Boolean saleable=true;// 是否上架
    private Boolean valid=true;// 是否有效，逻辑删除用

    private Date createTime;// 创建时间
    private Date lastUpdateTime;// 最后修改时间

    public Spu() {
    }

    public Spu( Long id,Long brandId, Long cid1, Long cid2, Long cid3, String title, String subTitle, Boolean saleable, Boolean valid, Date createTime, Date lastUpdateTime) {
        this.id = id;
        this.brandId = brandId;
        this.cid1 = cid1;
        this.cid2 = cid2;
        this.cid3 = cid3;
        this.title = title;
        this.subTitle = subTitle;
        this.saleable = saleable;
        this.valid = valid;
        this.createTime = createTime;
        this.lastUpdateTime = lastUpdateTime;
    }

}
