package com.leyou.order.controller;

import com.leyou.order.pojo.Order;
import com.leyou.order.service.OrderService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Auther: wdd
 * @Date: 2019/11/15 17:42
 * @Description:
 */
@Api(tags = "订单控制器")
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("")
    @ApiOperation(value = "创建订单接口，返回订单编号", notes = "创建订单")
    @ApiImplicitParam(name = "order", required = true, value = "订单的json对象,包含订单条目和物流信息")
    public ResponseEntity<String> createOrder(@RequestBody @Valid Order order) {
        Long id = orderService.createOrder(order);
        return ResponseEntity.ok(id + "");
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据订单编号查询订单，返回订单对象", notes = "查询订单")
    @ApiImplicitParam(name = "id", required = true, value = "订单的编号")
    public ResponseEntity<Order> queryOrderById(@PathVariable("id") Long id) {
        Order order = orderService.queryById(id);
        return ResponseEntity.ok(order);
    }

    @PutMapping("{id}/{status}")
    @ApiOperation(value = "更新订单状态", notes = "更新订单状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单编号", type = "Long"),
            @ApiImplicitParam(name = "status", value = "订单状态：1未付款，2已付款未发货，3已发货未确认，4已确认未评价，5交易关闭，6交易成功，已评价", type = "Integer"),
    })

    @ApiResponses({
            @ApiResponse(code = 204, message = "修改状态成功"),
            @ApiResponse(code = 400, message = "请求参数有误"),
            @ApiResponse(code = 500, message = "查询失败")
    })
    public ResponseEntity<Void> updateStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status) {
        orderService.updateStatus(id, status);
        // 返回204
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
