package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.util.JwtUtils;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: wdd
 * @Date: 2019/11/9 16:00
 * @Description:
 */
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @Autowired
    private JwtProperties prop;

    @Autowired
    private AuthService authService;


    /**
     * 用户登录授权
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/accredit")
    public ResponseEntity<Void> authentication(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletResponse response,
            HttpServletRequest request){
        // 登录校验
        String token = authService.authentication(username,password);
        if(StringUtils.isBlank(token)){
            throw new LyException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
        CookieUtils.setCookie(request,response,prop.getCookieName(),token,prop.getCookieMaxAge(), null, true);
        return ResponseEntity.ok().build();
    }

    /**
     * 管理员登录授权
     * @param name
     * @param password
     * @param response
     * @param request
     * @return
     */
    @PostMapping("/admin/accredit")
    public ResponseEntity<Void> adminAuthentication(
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            HttpServletResponse response,
            HttpServletRequest request){
        // 登录校验
        String token = authService.adminAuthentication(name,password);
        if(StringUtils.isBlank(token)){
            throw new LyException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
        CookieUtils.setCookie(request,response,prop.getCookieName(),token,prop.getCookieMaxAge(), null, true);
        return ResponseEntity.ok().build();
    }


    /**
     * 验证用户信息
     * @param token
     * @return
     */
    @GetMapping("/verify")
    public ResponseEntity<UserInfo> verifyUser(@CookieValue("LY_TOKEN")String token,
                                               HttpServletRequest request,
                                               HttpServletResponse response){
        try {
            // 从token中解析token信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
            // 解析成功要重新刷新token
            token = JwtUtils.generateToken(userInfo, prop.getPrivateKey(), prop.getExpire());
            // 更新cookie中的token
            CookieUtils.setCookie(request, response, prop.getCookieName(), token, prop.getCookieMaxAge());
            // 解析成功返回用户信息
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.INVALID_TOKEN);
        }
    }

}
