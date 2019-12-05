package com.leyou.seckill.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.CaseFormat;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.pojo.PageResult;
import com.leyou.seckill.client.GoodsClient;
import com.leyou.seckill.client.StockClient;
import com.leyou.seckill.mapper.SeckillMapper;
import com.leyou.seckill.pojo.Seckill;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: wdd
 * @Date: 2019/12/4 18:40
 * @Description:
 */
@Service
public class SeckillService {
    @Autowired
    private SeckillMapper seckillMapper;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private StockClient stockClient;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static String SECKILL_KEY = "SECKILL_KEY_";

    @Transactional
    public void addSeckill(Seckill seckill) {
        // 判断时间参数是否合法 即 结束时间大于开始时间 || 当前时间大于开始时间
        Date now = new Date();
        long diff = (seckill.getEndTime().getTime() - seckill.getStartTime().getTime()) / 1000;  // 过期时间,设置redis有用
        if (diff <= 0 || (now.getTime() - seckill.getStartTime().getTime()) / 1000 <= 0) {
            // 不合法
            throw new LyException(ExceptionEnum.INVALID_DATE);
        }
        // 判断库存参数是否合法 即 秒杀库存小于商品库存
        try {
            // 在内部判断参数是否合法
            stockClient.decreaseStock(seckill.getSkuId(), seckill.getNum());
        } catch (Exception e) {
            // 抛出异常则表明不合法
            throw new LyException(ExceptionEnum.INVALID_STOCK_NUM);
        }
        // 写入数据库
        int count = seckillMapper.insert(seckill);
        if (count != 1) {
            throw new LyException(ExceptionEnum.SECKILL_SAVE_ERROR);
        }
        // 获取该秒杀商品详细信息 即sku, 方便页面展示
        seckill.setSku(goodsClient.querySkuById(seckill.getSkuId()));
        // 以字符串的形式存储
        // 写入redis缓存并设置过期时间,方便读取,减轻数据库压力
        redisTemplate.opsForValue().set(SECKILL_KEY + seckill.getId(), JSON.toJSONString(seckill), diff, TimeUnit.SECONDS);

    }

    public PageResult<Seckill> querySeckillsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        // 初始化example对象
        Example example = new Example(Seckill.class);
        Example.Criteria criteria = example.createCriteria();

        // 根据name模糊查询，或者根据首字母查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("id", "%" + key + "%").orLike("skuId","%" + key + "%").orLike("title","%" + key + "%")
                    .orEqualTo("num",key).orEqualTo("price",key);
        }

        // 添加分页条件
        PageHelper.startPage(page, rows);

        // 添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            sortBy = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortBy);
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }

        List<Seckill> seckills = seckillMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(seckills)){
            throw new LyException(ExceptionEnum.SECKILL_NOT_FOUND);
        }

        //

        // 包装成pageInfo
        PageInfo<Seckill> pageInfo = new PageInfo<>(seckills);
        // 包装成分页结果集返回
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());

    }
}
