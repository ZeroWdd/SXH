package com.leyou.seckill.client;

import com.leyou.stock.api.StockApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: wdd
 * @Date: 2019/12/4 19:54
 * @Description:
 */
@FeignClient("stock-server")
public interface StockClient extends StockApi {
}
