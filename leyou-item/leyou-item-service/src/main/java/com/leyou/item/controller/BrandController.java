package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Classname BrandController
 * @Description None
 * @Date 2019/8/4 16:13
 * @Created by WDD
 */
@Api(tags  = "品牌控制器")
@Controller
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @ApiOperation(value = "条件分页查询品牌")
    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> queryBrandsByPage(
            @RequestParam(value = "key", required = false)String key,
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "5")Integer rows,
            @RequestParam(value = "sortBy", required = false)String sortBy,
            @RequestParam(value = "desc", required = false)Boolean desc){
        PageResult<Brand> result = brandService.queryBrandsByPage(key, page, rows, sortBy, desc);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "新增品牌")
    @PostMapping("")
    public ResponseEntity<Void> saveBrand( Brand brand, @RequestParam("cids") List<Long> cids){
        brandService.saveBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "根据分类查询品牌")
    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandsByCid(@PathVariable("cid")Long cid){
        List<Brand> brands = brandService.queryBrandsByCid(cid);
        return ResponseEntity.ok(brands);
    }

    @ApiOperation(value = "根据id查询品牌")
    @GetMapping("/{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id")Long id){
        Brand brand = brandService.queryBrandById(id);
        return ResponseEntity.ok(brand);
    }

    @PutMapping("")
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        brandService.updateBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("id")Long id){
        brandService.deleteBrand(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
