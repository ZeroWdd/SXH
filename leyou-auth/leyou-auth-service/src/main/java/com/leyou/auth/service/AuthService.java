package com.leyou.auth.service;

import com.leyou.admin.pojo.Admin;
import com.leyou.auth.client.AdminClient;
import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.util.JwtUtils;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: wdd
 * @Date: 2019/11/9 16:01
 * @Description:
 */
@Service
public class AuthService {

    @Autowired
    private UserClient userClient;
    @Autowired
    private AdminClient adminClient;

    @Autowired
    private JwtProperties properties;

    public String authentication(String username, String password) {
        try {
            // 调用微服务，执行查询
            User user = userClient.queryUser(username, password);
            // 如果查询结果为null，则直接返回null
            if (user == null) {
                return null;
            }
            // 如果有查询结果，则生成token
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()), properties.getPrivateKey(), properties.getExpire());
            return token;
        }catch (Exception e){
            throw new LyException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        }
    }

    public String adminAuthentication(String name, String password) {
        try {
            // 调用微服务，执行查询
            Admin admin = adminClient.queryAdmin(name, password);
            // 如果查询结果为null，则直接返回null
            if (admin == null) {
                return null;
            }
            // 如果有查询结果，则生成token
            String token = JwtUtils.generateToken(new UserInfo(admin.getAdminId(), admin.getName()), properties.getPrivateKey(), properties.getExpire());
            return token;
        }catch (Exception e){
            throw new LyException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        }
    }
}
