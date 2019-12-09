package com.leyou.seckill.pojo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.leyou.common.util.LongJsonDeserializer;
import com.leyou.common.util.LongJsonSerializer;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Auther: wdd
 * @Date: 2019/12/7 15:37
 * @Description:
 */
@Data
@Table(name = "tb_seckill_order")
public class SeckillOrder {
    @Id
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @JsonSerialize(using = LongJsonSerializer.class)
    private Long orderId;
    private Long userId;
    private Long addressId;
    private Long price;
    /**
     * 初始阶段：1、未付款、未发货；初始化所有数据
     * 付款阶段：2、已付款、未发货；更改付款时间
     * 发货阶段：3、已发货，未确认；更改发货时间、物流名称、物流单号
     * 成功阶段：4、已确认，未评价；更改交易结束时间
     * 关闭阶段：5、关闭； 更改更新时间，交易关闭时间。
     * 评价阶段：6、已评价
     */
    private Integer status;

    private Date createTime;// 创建时间

    private Date paymentTime;// 付款时间

    private Date consignTime;// 发货时间

    private Date endTime;// 交易结束时间

    private Date closeTime;// 交易关闭时间

    private Date commentTime;// 评价时间

    private Long skuId;// 商品id

    private String title; // 标题
}
