package com.leyou.seckill.controller;

import com.leyou.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: wdd
 * @Date: 2019/12/4 18:39
 * @Description:
 */
@RestController
public class SeckillController {
    @Autowired
    private SeckillService seckillService;
}
