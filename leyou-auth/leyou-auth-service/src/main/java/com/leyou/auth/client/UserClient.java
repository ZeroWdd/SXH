package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: wdd
 * @Date: 2019/11/9 16:27
 * @Description:
 */
@FeignClient(value = "user-service")
public interface UserClient extends UserApi {

}
