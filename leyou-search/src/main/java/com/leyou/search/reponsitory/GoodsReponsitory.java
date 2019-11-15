package com.leyou.search.reponsitory;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Auther: wdd
 * @Date: 2019/10/28 16:26
 * @Description:
 */
public interface GoodsReponsitory extends ElasticsearchRepository<Goods, Long> {
}
