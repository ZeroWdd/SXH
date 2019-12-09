package com.leyou.seckill.thread;

import com.alibaba.fastjson.JSON;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.util.IdWorker;
import com.leyou.seckill.mapper.SeckillOrderMapper;
import com.leyou.seckill.pojo.Seckill;
import com.leyou.seckill.pojo.SeckillOrder;
import com.leyou.seckill.vo.OrderRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Auther: wdd
 * @Date: 2019/12/9 15:27
 * @Description:
 */
@Component
public class CreateOrderThread {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    private static String SECKILL_KEY = "SECKILL_KEY_"; // key 加上seckill的id
    private static String SECKILL_USER_KEY = "SECKILL_USER_KEY_";

    @Async
    public void createOrder(){
        // 获取skuid
        String json = redisTemplate.boundListOps(OrderRecord.class.getSimpleName()).rightPop();
        OrderRecord orderRecord = JSON.parseObject(json, OrderRecord.class);
        // 获取 seckill
        json = redisTemplate.opsForValue().get(SECKILL_KEY + orderRecord.getSkuId());
        Seckill seckill = JSON.parseObject(json, Seckill.class);

        // 还有库存,创建订单
        SeckillOrder order = new SeckillOrder();
        //获取登录用户id
        order.setUserId(orderRecord.getUserId());
        // 生成id
        order.setOrderId(idWorker.nextId());
        // 金额
        order.setPrice(seckill.getPrice()); // 取单位分
        // 状态
        order.setStatus(1);
        // 时间
        order.setCreateTime(new Date());
        // 商品id
        order.setSkuId(seckill.getSkuId());
        // 标题
        order.setTitle(seckill.getTitle());
        // 存入数据库
        int count = seckillOrderMapper.insertSelective(order);
        if(count != 1){
            throw new LyException(ExceptionEnum.SECKILL_IS_ORVER);
        }
        // 标记该用户,防止多次抢购
        redisTemplate.boundSetOps(SECKILL_USER_KEY + orderRecord.getSkuId()).add(orderRecord.getUserId() + "");
    }
}
