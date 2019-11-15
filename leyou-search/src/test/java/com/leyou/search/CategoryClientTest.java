package com.leyou.search;

import com.leyou.LeyouSearchApplication;
import com.leyou.item.pojo.Sku;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeyouSearchApplication.class)
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void testQueryCategories() {
        List<String> names = this.categoryClient.queryNameByIds(Arrays.asList(1L, 2L, 3L));
        names.forEach(System.out::println);
    }

    @Test
    public void test1QueryCategories() {
        List<Sku> skus = this.goodsClient.querySkuBySpuId(new Long("2"));
        System.out.println(skus);
    }


}