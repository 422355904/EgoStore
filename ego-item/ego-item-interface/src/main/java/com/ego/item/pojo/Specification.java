package com.ego.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author TheKing
 * @Date 2019/9/27 13:53
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Data
@Table(name = "tb_specification") //规格参数
public class Specification {
    @Id
    private Long categoryId;
    private String specifications;
}
