package com.leyou.seckill.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Auther: wdd
 * @Date: 2019/12/7 15:37
 * @Description:
 */
@Data
@Table(name = "tb_seckill_order")
public class SeckillOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private Long userId;
    private Long addressId;
    private Long price;
    private Integer status; // 支付状态 0 未付款 1 已付款
}
