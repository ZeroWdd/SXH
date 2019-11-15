package com.leyou.order.controller;

import com.leyou.order.pojo.Address;
import com.leyou.order.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: wdd
 * @Date: 2019/11/15 11:05
 * @Description:
 */
@Api(tags  = "地址控制器")
@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping("")
    @ApiOperation("新增地址")
    public ResponseEntity<Long> addAddress(@RequestBody Address address){
        Long addressId = addressService.addAddress(address);
        return ResponseEntity.ok(addressId);
    }

    @PutMapping("")
    @ApiOperation("更改地址")
    public ResponseEntity<Void> updateAddress(@RequestBody Address address){
        addressService.updataAddress(address);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{addressId}")
    @ApiOperation("根据地址id查询地址")
    public ResponseEntity<Address> queryAddress(@PathVariable Long addressId){
        Address address = addressService.queryAddress(addressId);
        return ResponseEntity.ok(address);
    }

    @DeleteMapping("/{addressId}")
    @ApiOperation("根据地址id删除地址")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId){
        addressService.deleteAddress(addressId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
