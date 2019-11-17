package com.leyou.stock.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Auther: wdd
 * @Date: 2019/11/17 09:35
 * @Description:
 */
@Data
@Table(name = "tb_stock")
public class Stock {
    @Id
    private Long skuId;
    private Integer seckillStock; // 可秒杀库存
    private Integer seckillTotal; // 秒杀总数量
    private Integer stock; // 库存数量
}
