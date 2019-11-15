package com.leyou.search.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: wdd
 * @Date: 2019/10/26 20:07
 * @Description:
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
