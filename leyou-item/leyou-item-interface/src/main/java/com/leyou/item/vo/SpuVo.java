package com.leyou.item.vo;

import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import lombok.Data;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2019/10/24 16:30
 * @Description:
 */
@Data
public class SpuVo extends Spu {
    String cname;// 商品分类名称

    String bname;// 品牌名称

    SpuDetail spuDetail;// 商品详情

    List<Sku> skus;// sku列表
}
