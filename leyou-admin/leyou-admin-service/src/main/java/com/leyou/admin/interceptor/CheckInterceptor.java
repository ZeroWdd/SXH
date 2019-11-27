package com.leyou.admin.interceptor;

import com.leyou.admin.config.JwtProperties;
import com.leyou.admin.service.RoleService;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.util.JwtUtils;
import com.leyou.common.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2019/11/27 09:55
 * @Description:
 */
public class CheckInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RoleService roleService;

    private JwtProperties jwtProperties;

    public CheckInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    // 定义一个线程域，存放登录用户
    private static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 查询token
        String token = CookieUtils.getCookieValue(request, "LY_TOKEN");
        if (StringUtils.isBlank(token)) {
            // 未登录,返回401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        // 有token，查询用户信息
        try {
            // 解析成功，证明已经登录
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            // 获取路径
            String requestURI = request.getRequestURI();
            // 判断该管理员是否拥有该路径的权限，没有返回false
            List<String> uris = roleService.queryUri(userInfo.getId());
            for(String uri : uris){
                if(requestURI.startsWith(uri)){
                    // 放入线程域
                    tl.set(userInfo);
                    return true;
                }
            }
            return true;
        } catch (Exception e){
            // 抛出异常，证明未登录,返回401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
