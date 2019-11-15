package com.leyou.web.service;

import com.leyou.item.pojo.*;
import com.leyou.web.client.BrandClient;
import com.leyou.web.client.CategoryClient;
import com.leyou.web.client.GoodsClient;
import com.leyou.web.client.SpecificationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Auther: wdd
 * @Date: 2019/11/4 18:41
 * @Description:
 */
@Service
public class GoodsService {
    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    public Map<String, Object> loadData(Long spuId){
        Map<String, Object> map = new HashMap<>();
        //根据id查询spu对象
        Spu spu = goodsClient.querySpuById(spuId);
        // 查询spudetail
        SpuDetail spuDetail = goodsClient.querySpuDetailById(spuId);
        // 查询sku集合
        List<Sku> skus = goodsClient.querySkuBySpuId(spuId);
        // 查询分类
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = categoryClient.queryNameByIds(cids);
        List<Map<String, Object>> categories = new ArrayList<>();
        for (int i = 0; i < cids.size(); i++) {
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("id", cids.get(i));
            categoryMap.put("name", names.get(i));
            categories.add(categoryMap);
        }
        // 查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        // 查询规格参数组
        List<SpecGroup> groups = specificationClient.querySpecsByCid(spu.getCid3());
        // 查询特殊的规格参数
        List<SpecParam> params = specificationClient.queryParams(null, spu.getCid3(), null, null);
        Map<Long, String> paramMap = new HashMap<>();
        params.forEach(param -> {
            paramMap.put(param.getId(), param.getName());
        });
        // 封装spu
        map.put("spu", spu);
        // 封装spuDetail
        map.put("spuDetail", spuDetail);
        // 封装sku集合
        map.put("skus", skus);
        // 分类
        map.put("categories", categories);
        // 品牌
        map.put("brand", brand);
        // 规格参数组
        map.put("groups", groups);
        // 查询特殊规格参数
        map.put("paramMap", paramMap);
        return map;
    }
}
