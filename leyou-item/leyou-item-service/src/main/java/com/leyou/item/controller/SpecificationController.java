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
        List<SpecGroup> list = specificationService.querySpecsByCid(cid);
        return ResponseEntity.ok(list);
    }

    @ApiOperation(value = "新增规格组返回id")
    @PostMapping("/group")
    public ResponseEntity<String> addSpecGroup(@RequestBody SpecGroup specGroup){
        Long specGroupId = specificationService.addSpecGroup(specGroup);
        return ResponseEntity.ok(specGroupId + "");
    }

    @ApiOperation(value = "删除规格组")
    @DeleteMapping("/group/{id}")
    public ResponseEntity<Void> deleteSpecGroup(@PathVariable("id") Long id){
        specificationService.deleteSpecGroup(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "修改规格组")
    @PutMapping("/group")
    public ResponseEntity<Void> deleteSpecGroup(@RequestBody SpecGroup specGroup){
        specificationService.updateSpecGroup(specGroup);
        return ResponseEntity.ok().build();
    }
}
