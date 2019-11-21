package com.leyou.admin.controller;

import com.leyou.admin.pojo.Permission;
import com.leyou.admin.service.PermissionService;
import com.leyou.common.pojo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2019/11/21 15:55
 * @Description:
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "条件分页查询权限")
    @GetMapping("/page")
    public ResponseEntity<PageResult<Permission>> queryPermissionsByPage(
            @RequestParam(value = "key", required = false)String key,
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "10")Integer rows,
            @RequestParam(value = "sortBy", required = false)String sortBy,
            @RequestParam(value = "desc", required = false)Boolean desc){
        PageResult<Permission> result = permissionService.queryPermissionsByPage(key, page, rows, sortBy, desc);
        return ResponseEntity.ok(result);
    }

    @ApiOperation("查询所有权限")
    @GetMapping("/list")
    public ResponseEntity<List<Permission>> selectPermissionList() {
        List<Permission> permissions = permissionService.selectPermissionList();
        return ResponseEntity.ok(permissions);
    }

    @ApiOperation("根据id查询权限")
    @GetMapping("/{id}")
    public ResponseEntity<Permission> selectPermission(@PathVariable Long id) {
        Permission permission = permissionService.selectPermission(id);
        return ResponseEntity.ok(permission);
    }


    @ApiOperation("添加权限")
    @PostMapping("")
    public ResponseEntity<Void> addPermission(@Valid Permission permission) {
        permissionService.addPermission(permission);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("删除权限")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("更改权限信息")
    @PutMapping("")
    public ResponseEntity<Void> updatePermission(@Valid Permission permission) {
        permissionService.updatePermission(permission);
        return ResponseEntity.ok().build();
    }
}
