package com.leyou.admin.controller;

import com.leyou.admin.pojo.Admin;
import com.leyou.admin.service.AdminService;
import com.leyou.common.pojo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Auther: wdd
 * @Date: 2019/11/19 08:42
 * @Description:
 */
@Api
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @ApiOperation("根据管理员名和密码查询管理员")
    @GetMapping("/query")
    public ResponseEntity<Admin> queryAdmin(
            @RequestParam("name") String name,
            @RequestParam("password") String password
    ) {
        Admin admin = adminService.queryAdmin(name, password);
        return ResponseEntity.ok(admin);
    }

    @ApiOperation("根据管理id查询管理员")
    @GetMapping("/{id}")
    public ResponseEntity<Admin> selectAdmin(@PathVariable Long id) {
        Admin admin = adminService.selectAdmin(id);
        return ResponseEntity.ok(admin);
    }


    @ApiOperation("添加管理员")
    @PostMapping("")
    public ResponseEntity<Void> addAdmin(@Valid Admin admin) {
        adminService.addAdmin(admin);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("删除管理员")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("更改管理员信息")
    @PutMapping("")
    public ResponseEntity<Void> updateAdmin(@Valid Admin admin) {
        adminService.updateAdmin(admin);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "条件分页查询管理员")
    @GetMapping("/page")
    public ResponseEntity<PageResult<Admin>> queryAdminsByPage(
            @RequestParam(value = "key", required = false)String key,
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "10")Integer rows,
            @RequestParam(value = "sortBy", required = false)String sortBy,
            @RequestParam(value = "desc", required = false)Boolean desc){
        PageResult<Admin> result = adminService.queryAdminsByPage(key, page, rows, sortBy, desc);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/deal")
    public ResponseEntity<Void> dealAdminRole(@RequestBody Admin admin) {
        adminService.dealAdminRole(admin);
        return ResponseEntity.ok().build();
    }
}
