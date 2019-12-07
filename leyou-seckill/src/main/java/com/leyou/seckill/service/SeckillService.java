package com.leyou.seckill.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.CaseFormat;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.pojo.PageResult;
import com.leyou.common.util.IdWorker;
import com.leyou.item.pojo.Sku;
import com.leyou.seckill.client.GoodsClient;
import com.leyou.seckill.client.StockClient;
import com.leyou.seckill.interceptor.LoginInterceptor;
import com.leyou.seckill.mapper.SeckillMapper;
import com.leyou.seckill.mapper.SeckillOrderMapper;
import com.leyou.seckill.pojo.Seckill;
import com.leyou.seckill.pojo.SeckillOrder;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
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
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private StockClient stockClient;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IdWorker idWorker;

    private static String SECKILL_KEY = "SECKILL_KEY_"; // key 加上seckill的id
    private static String SECKILL_LIST = "SECKILL_LIST"; //
    private static String SECKILL_NUM = "SECKILL_NUM_"; //


    @Transactional
    public void addSeckill(Seckill seckill) {
        // 判断时间参数是否合法 即 结束时间大于开始时间 || 当前时间小于开始时间
        Date now = new Date();
        long diff = (seckill.getEndTime().getTime() - now.getTime()) / 1000;  // 过期时间,设置redis有用
        if ((seckill.getEndTime().getTime() - seckill.getStartTime().getTime()) / 1000 <= 0 || (now.getTime() - seckill.getStartTime().getTime()) / 1000 >= 0) {
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
        seckill.setStatus(0); // 设置有效
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

        // 判断取出的秒杀项目在当前是否过期
        int count = 0;
        Date now = new Date();
        for(Seckill seckill : seckills){
            if(!seckill.getStatus().equals(1)){ // 数据库为1, 但有可能过期,进行比对
                if(now.getTime() - seckill.getEndTime().getTime() >= 0){
                    // 过期
                    seckill.setStatus(1);
                    count = seckillMapper.updateByPrimaryKey(seckill);
                    if(count != 1){
                        throw new LyException(ExceptionEnum.SECKILL_UPDATE_ERROR);
                    }
                    // 将秒杀库存还给总库存
                    Map<String, Object> map = new HashMap<>();
                    map.put("skuId",seckill.getSkuId());
                    map.put("num",seckill.getNum());
                    rabbitTemplate.convertAndSend("leyou.stock.exchange","stock.update",map);
                }
            }
        }

        // 包装成pageInfo
        PageInfo<Seckill> pageInfo = new PageInfo<>(seckills);
        // 包装成分页结果集返回
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());

    }

    public List<Sku> querySkuBySpuId(Long spuId) {
        // 1.通过spuId获取sku列表
        List<Sku> skus = goodsClient.querySkuBySpuId(spuId);
        // 2.对sku列表进行筛选 选择可以添加的sku
        // 2.1先判断数据库中是否已存在sku秒杀商品并未过期,存在则将无法添加
        for(Iterator<Sku> it = skus.iterator(); it.hasNext();){
            Sku sku = it.next();
            Seckill seckill = new Seckill();
            seckill.setStatus(0); // 未过期
            seckill.setSkuId(sku.getId());
            Seckill one = seckillMapper.selectOne(seckill);
            if(!org.springframework.util.StringUtils.isEmpty(one)){ // 不为空,表明存在,即未过期,不可添加
                // 2.2有可能redis与数据库尚未同步,即redis已过期
                String json = redisTemplate.opsForValue().get(SECKILL_KEY + one.getId());
                if(StringUtils.isEmpty(json)){
                    // 为空,表明过期,可添加,并同步数据库
                    seckill.setStatus(1); // 过期
                    seckillMapper.insertSelective(seckill);
                    continue; // 结束本次循环
                }
                // 不为空
                it.remove();
            }
        }
        // 剩下的sku列表方可添加
        return skus;
    }

    public List<Seckill> querySeckills() {
        List<Seckill> select = new ArrayList<>();
        // 1.先判断redis是否有
        String json = redisTemplate.opsForValue().get(SECKILL_LIST);
        if(StringUtils.isEmpty(json)){
            // 2.从数据库中查询有效的秒杀商品再前台展示
            Seckill seckill = new Seckill();
            seckill.setStatus(0);
            select = seckillMapper.select(seckill);
            if(CollectionUtils.isEmpty(select)){
                throw new LyException(ExceptionEnum.SECKILL_NOT_FOUND);
            }
            // 获取sku
            for(Seckill s : select){
                Sku sku = goodsClient.querySkuById(s.getSkuId());
                s.setSku(sku);
            }
            // 将数据存入redis,设置过期时间5分钟
            redisTemplate.opsForValue().set(SECKILL_LIST,JSON.toJSONString(select),5,TimeUnit.MINUTES);
            return select;
        }
        select = JSON.parseArray(json, Seckill.class);
        return select;
    }

    public Seckill querySeckill(Long id) {
        // redis中读取
        String json = redisTemplate.opsForValue().get(SECKILL_KEY + id);
        // 可能数据据丢失
        if(StringUtils.isEmpty(json)){
            // 曲数据库查询
            Seckill seckill = seckillMapper.selectByPrimaryKey(id);
            // sku
            Sku sku = goodsClient.querySkuById(seckill.getSkuId());
            seckill.setSku(sku);
            // 保存到redis
            Long diff = seckill.getEndTime().getTime() - new Date().getTime();
            redisTemplate.opsForValue().set(SECKILL_KEY + seckill.getId(), JSON.toJSONString(seckill), diff, TimeUnit.SECONDS);
            return seckill;
        }
        Seckill seckill = JSON.parseObject(json, Seckill.class);
        return seckill;
    }

    public void createOrder(Long id) {
        // 从redis中取出对应秒杀商品
        String json = redisTemplate.opsForValue().get(SECKILL_KEY + id);
        if(!StringUtils.isEmpty(json)){
            Seckill seckill = JSON.parseObject(json, Seckill.class);
            // 获取库存
            Integer num = seckill.getNum();
            // 通过redis计数器
            if(redisTemplate.opsForValue().increment(SECKILL_NUM+id,1) <= num){
                // 还有库存,创建订单
                SeckillOrder order = new SeckillOrder();
                //获取登录用户id
                order.setUserId(LoginInterceptor.getLoginUser().getId());
                // 生成id
                order.setOrderId(idWorker.nextId());
                // 金额
                order.setPrice(seckill.getPrice() * 100); // 取单位分
                // 状态
                order.setStatus(0);

                int count = seckillOrderMapper.insertSelective(order);
                if(count != 1){
                    throw new LyException(ExceptionEnum.SECKILL_IS_ORVER);
                }
            }else{
                throw new LyException(ExceptionEnum.SECKILL_IS_ORVER);
            }

        }else{
            // 没有
            throw new LyException(ExceptionEnum.SECKILL_EXPIRES);
        }
    }
}
