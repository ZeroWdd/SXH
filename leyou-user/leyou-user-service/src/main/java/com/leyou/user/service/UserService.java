package com.leyou.user.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.pojo.PageResult;
import com.leyou.common.util.CodecUtils;
import com.leyou.common.util.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    public PageResult<User> queryUsersByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        // 初始化example对象
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();

        // 根据name模糊查询，或者根据电话模糊查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("username", "%" + key + "%").orLike("phone","%" + key + "%");
        }

        // 添加分页条件
        PageHelper.startPage(page, rows);

        // 添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }

        List<User> users = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(users)){
            throw new LyException(ExceptionEnum.USER_NOT_FOUND);
        }
        // 包装成pageInfo
        PageInfo<User> pageInfo = new PageInfo<>(users);
        // 包装成分页结果集返回
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    public void deleteUser(Long id) {
        int count = userMapper.deleteByPrimaryKey(id);
        if(count != 1){
            throw new LyException(ExceptionEnum.USER_DELETE_ERROR);
        }
    }

    public User queryUserById(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        if(org.springframework.util.StringUtils.isEmpty(user)){
            throw new LyException(ExceptionEnum.USER_NOT_FOUND);
        }
        return user;
    }

    public void updateUserPassword(Long id , String password) {
        // 先查询user获取盐
        User u = userMapper.selectByPrimaryKey(id);
        // 对密码加密
        u.setPassword(CodecUtils.md5Hex(password, u.getSalt()));
        int count = userMapper.updateByPrimaryKey(u);
        if(count != 1){
            throw new LyException(ExceptionEnum.USER_UPDATE_ERROR);
        }
    }
}