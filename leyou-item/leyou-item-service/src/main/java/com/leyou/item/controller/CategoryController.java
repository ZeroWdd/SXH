package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Classname CategoryController
 * @Description None
 * @Date 2019/8/3 12:34
 * @Created by WDD
 */
@Api(tags = "分类控制器")
@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "根据父id查询子节点")
    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryCategoriesByPid(@RequestParam(value = "pid",defaultValue = "0") Long pid){
        if(pid == null || pid < 0){
            //返回400,参数异常
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
        List<Category> categories = categoryService.queryCategoriesByPid(pid);
        return ResponseEntity.ok(categories);
    }

    @ApiOperation(value = "根据ids查询分类名称")
    @GetMapping("/names")
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids")List<Long> ids){

        List<String> names = this.categoryService.queryNamesByIds(ids);
        if (CollectionUtils.isEmpty(names)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(names);
    }


    @ApiOperation(value = "根据id删除节点")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "新增节点")
    @PostMapping("/")
    public ResponseEntity<Void> addCategory(@RequestBody Category category){
        categoryService.addCategory(category);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "修改节点")
    @PutMapping("/")
    public ResponseEntity<Void> updateCategory(@RequestBody Category category){
        categoryService.updateCategory(category);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "根据3级分类id，查询1~3级的分类")
    @GetMapping("all/level")
    public ResponseEntity<List<Category>> queryAllByCid3(@RequestParam("id") Long id){
        List<Category> list = categoryService.queryAllByCid3(id);
        return ResponseEntity.ok(list);
    }
}
