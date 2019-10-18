package com.ego.user.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author TheKing
 * @Date 2019/10/16 17:37
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Data
@Table(name = "tb_addrs")
public class Addrs {
    @Id
    @KeySql(useGeneratedKeys = true)
    Integer id;
    String name;
    String addrDetail;
    String tel;
    String email;
}
