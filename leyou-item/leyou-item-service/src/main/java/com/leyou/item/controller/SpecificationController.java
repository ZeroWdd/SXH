package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2019/10/23 21:13
 * @Description:
 */
@Api(tags = "规格组值控制器")
@RestController
@RequestMapping("/spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    @ApiOperation(value = "根据分类id查询规格组")
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsByCid(@PathVariable("cid")Long cid){
        List<SpecGroup> list =  specificationService.queryGroupsByCid(cid);
        return ResponseEntity.ok(list);
    }

    @ApiOperation(value = "查询规格")
    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> queryParamsList(@RequestParam(value = "gid",required = false)Long gid,
                                                           @RequestParam(value = "cid",required = false)Long cid,
                                                           @RequestParam(value = "generic", required = false)Boolean generic,
                                                           @RequestParam(value = "searching",required = false)Boolean searching){
        List<SpecParam>  list = specificationService.queryParamsList(gid,cid,generic,searching);
        return ResponseEntity.ok(list);
    }

    @ApiOperation(value = "查询规格组及组内参数")
    @GetMapping("{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecsByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> list = this.specificationService.querySpecsByCid(cid);
        return ResponseEntity.ok(list);
    }
}
