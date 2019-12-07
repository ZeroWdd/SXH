package com.leyou.seckill.api;

import com.leyou.seckill.pojo.Seckill;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Auther: wdd
 * @Date: 2019/12/7 09:42
 * @Description:
 */
public interface SeckillApi {
    @GetMapping("/{id}")
    Seckill querySeckill(@PathVariable("id") Long id);
}
