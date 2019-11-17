package com.leyou.api;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: wdd
 * @Date: 2019/11/17 10:18
 * @Description:
 */
public interface StockApi {
    @PutMapping("")
    void decreaseStock(@RequestParam Long skuId, @RequestParam Integer num);
}
