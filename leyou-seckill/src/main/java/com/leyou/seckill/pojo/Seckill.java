package com.leyou.seckill.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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

    private Long spu_id;
    private Long sku_id;
    private Date startTime; // 开始时间
    private Date endTime; // 结束时间
    private String introduction; // 描述
}
