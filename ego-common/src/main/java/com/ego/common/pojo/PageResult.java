package com.ego.common.pojo;

import lombok.Data;

import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/9/24 22:28
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Data
public class PageResult<T> {
    private Long totalPage; //总页数
    private Long total; //总条数
    private List<T> items; //数据集合

    public PageResult() {
    }

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }
}
