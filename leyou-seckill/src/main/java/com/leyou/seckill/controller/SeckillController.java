package com.leyou.seckill.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.seckill.pojo.Seckill;
import com.leyou.seckill.service.SeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("分页条件查询秒杀商品")
    @GetMapping("/page")
    public ResponseEntity<PageResult<Seckill>> querySeckillsByPage(
            @RequestParam(value = "key", required = false)String key,
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "5")Integer rows,
            @RequestParam(value = "sortBy", required = false)String sortBy,
            @RequestParam(value = "desc", required = false)Boolean desc){
        PageResult<Seckill> result = seckillService.querySeckillsByPage(key, page, rows, sortBy, desc);
        return ResponseEntity.ok(result);
    }
}
