package com.ego.order.mapper;

import com.ego.order.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author yaorange
 * @date 2019/03/01
 */
@Repository
public interface OrderMapper extends Mapper<Order> {
//    @Select("select o.* from tb_order o left join tb_order_status os on o.order_id = os.order_id where os.status = #{status} ")
    /*@Select("<script>" +
            "select o.*,os.`status`,os.payment_time from tb_order o,tb_order_status os where o.order_id=os.order_id "+
            "<if test='status!=null'>"+
            "and os.status = #{status} "+
            "</if>"+
            "</script>")*/
    List<Order> selectByStatus(@Param("status") Integer status);
}
