package com.leyou.stock.mapper;

import com.leyou.stock.pojo.Stock;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Auther: wdd
 * @Date: 2019/11/16 21:11
 * @Description:
 */
public interface StockMapper extends Mapper<Stock> {

    @Update("UPDATE tb_stock SET stock = stock - #{num} WHERE sku_id = #{skuId} AND stock > 0; ")
    int decreaseStock(@Param("skuId") Long skuId, @Param("num") Integer num);
}
