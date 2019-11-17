package com.leyou.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: wdd
 * @Date: 2019/11/16 21:11
 * @Description:
 */
@Service
public class StockService {

    @Autowired
    private StockMapper stockMapper;

    public void decreaseStockByskuId(Long skuId,Integer num) {
        int count = stockMapper.decreaseStock(skuId,num);
        if(count == 0){
            throw new LyException(ExceptionEnum.STOCK_LACK_ERROR);
        }
    }
}
