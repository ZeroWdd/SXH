package com.leyou.seckill.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: wdd
 * @Date: 2019/12/4 19:37
 * @Description:
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}

