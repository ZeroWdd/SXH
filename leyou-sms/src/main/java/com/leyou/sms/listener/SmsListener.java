package com.leyou.sms.listener;

import com.alibaba.fastjson.JSONObject;
import com.leyou.sms.util.SmsUtil;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2019/11/6 09:27
 * @Description:
 */
@Component
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "sms.verify.code.queue", durable = "true"),
            exchange = @Exchange(
                    value = "leyou.sms.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = "sms.verify.code"))
    public void listenCreate(Map<String,String> msg) throws Exception {
        if(CollectionUtils.isEmpty(msg)){
            return;
        }
        String phone = msg.remove("phone");
        if(StringUtils.isEmpty(phone)){
            return;
        }
        smsUtil.sendSms(phone,JSONObject.toJSONString(msg));
    }

}
