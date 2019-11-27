package com.leyou.auth.client;

import com.leyou.admin.api.AdminApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: wdd
 * @Date: 2019/11/23 10:30
 * @Description:
 */
@FeignClient(value = "admin-service")
public interface AdminClient extends AdminApi {
}
