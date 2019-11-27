package com.leyou.admin.api;

import com.leyou.admin.pojo.Admin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: wdd
 * @Date: 2019/11/23 10:28
 * @Description:
 */
@RequestMapping("/admin")
public interface AdminApi {
    @GetMapping("/query")
    Admin queryAdmin(
            @RequestParam("name") String name,
            @RequestParam("password") String password);
}
