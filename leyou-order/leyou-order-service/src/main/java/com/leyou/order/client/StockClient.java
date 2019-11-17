package com.leyou.order.client;

import com.leyou.stock.api.StockApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: wdd
 * @Date: 2019/11/17 10:23
 * @Description:
 */
@FeignClient("stock-service")
public interface StockClient extends StockApi {
}
