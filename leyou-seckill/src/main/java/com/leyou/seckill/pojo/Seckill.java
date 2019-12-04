package com.leyou.seckill.pojo;

import com.leyou.item.pojo.Sku;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Auther: wdd
 * @Date: 2019/12/4 18:43
 * @Description:
 */
@Data
@Table(name = "tb_seckill")
public class Seckill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long spuId;
    private Long skuId;
    private Long price; // 秒杀价格
    private Date startTime; // 开始时间
    private Date endTime; // 结束时间
    private String introduction; // 描述
    private Integer num; // 秒杀库存

    @Transient
    private Sku sku;
}
