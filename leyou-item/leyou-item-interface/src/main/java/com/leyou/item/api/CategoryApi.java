package com.leyou.item.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2019/10/27 09:10
 * @Description:
 */
@RequestMapping("/category")
public interface CategoryApi {

    @GetMapping("/names")
    List<String> queryNameByIds(@RequestParam("ids") List<Long> ids);

}