package com.ego.item.pojo;

import lombok.Data;

/**
 * @Author TheKing
 * @Date 2019/10/18 15:06
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */

public enum TypeEnum {
    NO_PROMOTION(0,"普通促销"),
    FULL_PROMOTION(1,"满减"),
    SEND_PROMOTION(2,"满额送抵扣券"),
    SECKILL_PROMOTION(3,"秒杀"),
    ;

    private Integer type;
    private String message;

    TypeEnum(Integer type, String message) {
        this.type = type;
        this.message = message;
    }

    public Integer value() {
        return type;
    }

    public String msg() {
        return message;
    }

    @Override
    public String toString() {
        return "TypeEnum{" +
                "type=" + type +
                ", message='" + message + '\'' +
                '}';
    }}
