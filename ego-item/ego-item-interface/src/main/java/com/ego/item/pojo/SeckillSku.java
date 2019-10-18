package com.ego.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "tb_seckill_sku")
public class SeckillSku {

  @Id
  @KeySql(useGeneratedKeys = true)
  private Long id;
  private Long skuId;
  private Date startTime;
  private Date endTime;
  private Double seckillPrice;
  private String title;
  private String image;
  private String enable;
  private Double price;

}
