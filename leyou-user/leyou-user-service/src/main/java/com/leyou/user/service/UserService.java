package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.util.CodecUtils;
import com.leyou.common.util.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;

    static final String KEY_PREFIX = "user:code:phone:";

    public Boolean checkData(String data, Integer type) {
        User user = new User();
        switch (type){
            case 1 : user.setUsername(data); break;
            case 2 : user.setPhone(data); break;
            default: throw new LyException(ExceptionEnum.INVALID_USER_TYPE);
        }
        return userMapper.selectCount(user) != 1;
    }

    public void sendVerifyCode(String phone) {
        //生成验证码
        String code = NumberUtils.generateCode(6);
        try {
            //发送短信
            Map<String, String> msg = new HashMap<>();
            msg.put("phone",phone);
            msg.put("code",code);
            amqpTemplate.convertAndSend("leyou.sms.exchange","sms.verify.code",msg);
            // 将code存入redis
            redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
        }catch (Exception e){
            log.error("发送短信失败。phone：{}， code：{}", phone, code);
            throw new LyException(ExceptionEnum.PHONE_SEND_ERROR);
        }
    }

    public void register(User user, String code) {
        // 校验短信验证码
        String cacheCode = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if(!code.equals(cacheCode)){
            throw new LyException(ExceptionEnum.INVALID_VERIFY_CODE);
            //return;
        }
        // 生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        // 对密码加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        // 强制设置不能指定的参数为null
        user.setId(null);
        user.setCreated(new Date());
        int count = userMapper.insertSelective(user);
        if(count != 1){
            throw new LyException(ExceptionEnum.INVALID_VERIFY_CODE);
        }
    }

    public User queryUser(String username, String password) {
        //查询
        User user = new User();
        user.setUsername(username);
        User one = userMapper.selectOne(user);
        //校验用户名和校验密码
        if(one == null || !one.getPassword().equals(CodecUtils.md5Hex(password,one.getSalt()))){
            throw new LyException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        return one;
    }
}