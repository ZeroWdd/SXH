package com.leyou.web.client;

import com.leyou.seckill.api.SeckillApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: wdd
 * @Date: 2019/12/7 09:47
 * @Description:
 */
@FeignClient(value = "seckill-service")
public interface SeckillClient extends SeckillApi {
}
