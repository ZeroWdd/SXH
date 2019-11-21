package com.leyou.admin.controller;

import com.leyou.admin.pojo.Admin;
import com.leyou.admin.pojo.Role;
import com.leyou.admin.service.RoleService;
import com.leyou.common.pojo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2019/11/19 15:55
 * @Description:
 */
@Api(tags = "")
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "条件分页查询角色")
    @GetMapping("/page")
    public ResponseEntity<PageResult<Role>> queryRolesByPage(
            @RequestParam(value = "key", required = false)String key,
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "10")Integer rows,
            @RequestParam(value = "sortBy", required = false)String sortBy,
            @RequestParam(value = "desc", required = false)Boolean desc){
        PageResult<Role> result = roleService.queryRolesByPage(key, page, rows, sortBy, desc);
        return ResponseEntity.ok(result);
    }

    @ApiOperation("查询所有角色")
    @GetMapping("/list")
    public ResponseEntity<List<Role>> selectRoleList() {
        List<Role> roles = roleService.selectRoleList();
        return ResponseEntity.ok(roles);
    }

    @ApiOperation("根据id查询角色")
    @GetMapping("/{id}")
    public ResponseEntity<Role> selectRole(@PathVariable Long id) {
        Role role = roleService.selectRole(id);
        return ResponseEntity.ok(role);
    }


    @ApiOperation("添加角色")
    @PostMapping("")
    public ResponseEntity<Void> addRole(@Valid Role role) {
        roleService.addRole(role);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("更改角色信息")
    @PutMapping("")
    public ResponseEntity<Void> updateRole(@Valid Role role) {
        roleService.updateRole(role);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("给角色分配信息")
    @PutMapping("/deal")
    public ResponseEntity<Void> dealRolePermission(@RequestBody Role role) {
        roleService.dealRolePermission(role);
        return ResponseEntity.ok().build();
    }
}
