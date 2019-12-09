package com.leyou.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther: wdd
 * @Date: 2019/12/9 15:46
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRecord implements Serializable {
    private Long skuId;
    private Long userId;
}
