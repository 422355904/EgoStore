package com.ego.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "tb_promotion")
public class Promotion {

  @Id
  @KeySql(useGeneratedKeys = true)
  private Long id;
  private String type;  //活动类型，目前可选的有：0没有促销，1满减，2满额送抵扣券，3秒杀
  private String description; //促销活动说明
  @Column(name = "`condition`")
  private Long condition;  //满减条件，秒杀的话设置为0
  private Long reduction;  //满减后的折扣金额
  private Long seckillPrice;  //秒杀价格，如果是秒杀活动，需要填写
  private Long couponId;  //满额送券，关联的优惠券id
  private String targets;  //作用的目标sku的id拼接，以','拼接
  private Date startTime;
  private Date endTime;

}
