package com.leyou.seckill.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.seckill.pojo.Seckill;
import com.leyou.seckill.service.SeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ApiOperation("分页条件查询秒杀商品,用于后台显示")
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

    @ApiOperation("显示可以添加的秒杀商品")
    @GetMapping("/sku/{spuId}")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@PathVariable("spuId") Long spuId){
        List<Sku> skus = seckillService.querySkuBySpuId(spuId);
        return ResponseEntity.ok(skus);
    }

    @ApiOperation("查询秒杀商品,用于前台显示")
    @GetMapping("")
    public ResponseEntity<List<Seckill>> querySeckills(){
        List<Seckill> result = seckillService.querySeckills();
        return ResponseEntity.ok(result);
    }


    @ApiOperation("根据Id查询秒杀商品,用于前台显示")
    @GetMapping("/{id}")
    public ResponseEntity<Seckill> querySeckill(@PathVariable("id") Long id){
        Seckill result = seckillService.querySeckill(id);
        return ResponseEntity.ok(result);
    }
}
