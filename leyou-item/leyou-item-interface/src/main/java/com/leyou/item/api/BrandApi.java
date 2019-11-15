package com.leyou.item.api;

import com.leyou.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: wdd
 * @Date: 2019/10/27 09:06
 * @Description:
 */
@RequestMapping("/brand")
public interface BrandApi {

    @GetMapping("/{id}")
    Brand queryBrandById(@PathVariable("id") Long id);
}
