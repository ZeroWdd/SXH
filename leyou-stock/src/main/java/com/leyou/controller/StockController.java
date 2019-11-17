package com.leyou.controller;

import com.leyou.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: wdd
 * @Date: 2019/11/16 21:10
 * @Description:
 */
@RestController
public class StockController {
    @Autowired
    private StockService stockService;

    @PutMapping("")
    public ResponseEntity<Void> decreaseStock(@RequestParam Long skuId, @RequestParam Integer num){
        stockService.decreaseStockByskuId(skuId,num);
        return ResponseEntity.ok().build();
    }
}
