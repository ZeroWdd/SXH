package com.leyou.controller;

import com.leyou.pojo.Cart;
import com.leyou.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2019/11/12 08:40
 * @Description:
 */
@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * 添加购物车
     *
     * @return
     */
    @PostMapping("")
    public ResponseEntity<Void> addCart(@RequestBody List<Cart> cartList) {
        cartService.addCart(cartList);
        return ResponseEntity.ok().build();
    }

    /**
     * 查询购物车列表
     *
     * @return
     */
    @GetMapping("")
    public ResponseEntity<List<Cart>> queryCartList() {
        List<Cart> carts = cartService.queryCartList();
        return ResponseEntity.ok(carts);
    }

    /**
     * 更改数量
     * @param cart
     * @return
     */
    @PutMapping("")
    public ResponseEntity<Void> updateNum(@RequestBody Cart cart){
        cartService.updateCarts(cart);
        return ResponseEntity.noContent().build();
    }

    /**
     * 删除购物车中商品
     * @param skuId
     * @return
     */
    @DeleteMapping("/{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") String skuId) {
        cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }

}
