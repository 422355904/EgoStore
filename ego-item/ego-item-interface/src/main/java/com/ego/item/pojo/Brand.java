package com.ego.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_brand")
public class Brand {

  @Id
  @KeySql(useGeneratedKeys = true)
  private long id;
  private String name;
  private String image;
  private String letter;

}
