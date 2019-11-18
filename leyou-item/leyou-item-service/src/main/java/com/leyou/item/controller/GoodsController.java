package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import com.leyou.item.vo.SpuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2019/10/24 16:31
 * @Description:
 */
@Api(tags = "商品控制器")
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @ApiOperation(value = "条件分页查询商品")
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuVo>> querySpuBoByPage(
            @RequestParam(value = "key", required = false)String key,
            @RequestParam(value = "saleable", required = false)Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "5")Integer rows){
        PageResult<SpuVo> pageResult = goodsService.querySpuVoByPage(key, saleable, page, rows);
        return ResponseEntity.ok(pageResult);
    }

    @ApiOperation(value = "添加商品")
    @PostMapping("/goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuVo spuVo){
        goodsService.saveGoods(spuVo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "查询商品SpuDetail")
    @GetMapping("/spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId")Long spuId){
        SpuDetail spuDetail = goodsService.querySpuDetailById(spuId);
        return ResponseEntity.ok(spuDetail);
    }

    @ApiOperation(value = "查询商品sku")
    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkusBySpuId(@RequestParam("id")Long spuId){
        List<Sku> skus = goodsService.querySkusBySpuId(spuId);
        return ResponseEntity.ok(skus);
    }

    @ApiOperation(value = "修改商品")
    @PutMapping("/goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuVo spuVo){
        goodsService.updateGoods(spuVo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation(value = "根据id查询spu")
    @GetMapping("/spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
        Spu spu = goodsService.querySpuById(id);
        return ResponseEntity.ok(spu);
    }

    @ApiOperation(value = "根据id查询sku")
    @GetMapping("/sku/{id}")
    public ResponseEntity<Sku> querySkuById(@PathVariable("id") Long id){
        Sku sku = goodsService.querySkuById(id);
        return ResponseEntity.ok(sku);
    }

    @ApiOperation(value = "根据id更改上架或下架")
    @PutMapping("/spu/shelf")
    public ResponseEntity<Void> updateSkuShelfById(@RequestBody Spu spu){
        goodsService.updateSkuShelf(spu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
