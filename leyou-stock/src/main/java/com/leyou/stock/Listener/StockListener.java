package com.leyou.stock.Listener;

import com.leyou.stock.service.StockService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2019/12/6 09:36
 * @Description:
 */
@Component
public class StockListener {

    @Autowired
    private StockService stockService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "stock.queue", durable = "true"),
            exchange = @Exchange(
                    value = "leyou.stock.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = "stock.update"))
    public void listenCreate(Map<String,Object> msg) throws Exception {
        if(CollectionUtils.isEmpty(msg)){
            return;
        }
        // 对库存进行添加 参数 skuId num
        stockService.increaseStockByskuId((Long)msg.get("skuId"),(Integer) msg.get("num"));
    }
}
