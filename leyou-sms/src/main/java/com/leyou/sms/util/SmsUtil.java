package com.leyou.sms.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: wdd
 * @Date: 2019/11/7 19:07
 * @Description:
 */
@Slf4j
@Component
public class SmsUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${aliyun.sms.accessKeyId}")
    public String accessKeyId;

    @Value("${aliyun.sms.accessKeySecret}")
    public String accessKeySecret;

    @Value("${aliyun.sms.singnName}")
    public String singnName;

    @Value("${aliyun.sms.verifyCodeTemplate}")
    public String verifyCodeTemplate;

    private static final String KEY_PREFIX = "sms:phone";
    private static final long SMS_MIN_INTERVAL_IN_MILLIS = 60000;

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    public SendSmsResponse sendSms(String phoneNumber,String templateParam){
        //存入redis的key
        String key = KEY_PREFIX + phoneNumber;

        //读取时间,限流
        String lastTime = redisTemplate.opsForValue().get(key);
        if(!StringUtils.isEmpty(lastTime)){
            Long last = Long.valueOf(lastTime);
            if(System.currentTimeMillis() - last < SMS_MIN_INTERVAL_IN_MILLIS){
                return null;
            }
        }
        try {
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            request.setMethod(MethodType.POST);
            //必填:待发送手机号
            request.setPhoneNumbers(phoneNumber);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(singnName);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(verifyCodeTemplate);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam(templateParam);

            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

            if(!"OK".equals(sendSmsResponse.getCode())){
                log.info("[短信服务] 发送短信失败, phoneNumber:{}, 原因:{}",phoneNumber,sendSmsResponse.getMessage());
            }
            //发送短信成功后，存入redis
            redisTemplate.opsForValue().set(key,String.valueOf(System.currentTimeMillis()),1, TimeUnit.MINUTES);
            return sendSmsResponse;
        }catch (Exception e){
            log.error("[短信服务] 发送短信异常, phoneNumber:{}",phoneNumber,e);
            return null;
        }
    }
}
