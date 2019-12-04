package com.leyou.seckill.controller;

import com.leyou.seckill.pojo.Seckill;
import com.leyou.seckill.service.SeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: wdd
 * @Date: 2019/12/4 18:39
 * @Description:
 */
@RestController
@Api(tags = "秒杀控制器")
public class SeckillController {
    @Autowired
    private SeckillService seckillService;

    @ApiOperation("添加秒杀商品")
    @PostMapping("")
    public ResponseEntity<Void> addSeckill(@RequestBody Seckill seckill){
        seckillService.addSeckill(seckill);
        return ResponseEntity.ok().build();
    }
}
