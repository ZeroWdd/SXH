package com.leyou.order.mapper;

import com.leyou.order.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2019/11/15 19:03
 * @Description:
 */
public interface OrderMapper extends Mapper<Order> {

    @Select("select tb_order.*,tb_order_status.status " +
            "from tb_order,tb_order_status " +
            "where tb_order.order_id = tb_order_status.order_id and tb_order.user_id = #{userId}")
    List<Order> queryOrderList(@Param("userId") Long userId);

    @Select("select tb_order.*,tb_order_status.status from tb_order,tb_order_status where tb_order.order_id = tb_order_status.order_id and tb_order.user_id = #{userId} and tb_order_status.status = #{status}")
    List<Order> queryOrderListByStatus(@Param("userId") Long userId, @Param("status") Integer status);
}
