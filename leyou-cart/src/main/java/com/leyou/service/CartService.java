package com.leyou.service;

import com.alibaba.fastjson.JSONObject;
import com.leyou.auth.entity.UserInfo;
import com.leyou.client.GoodsClient;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.interceptor.LoginInterceptor;
import com.leyou.item.pojo.Sku;
import com.leyou.pojo.Cart;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: wdd
 * @Date: 2019/11/12 08:41
 * @Description:
 */
@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private GoodsClient goodsClient;

    static final String KEY_PREFIX = "leyou:cart:uid:";

    public void addCart(List<Cart> cartList) {
        // 获取用户登录信息
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        // 设置Redis的key
        String key = KEY_PREFIX + userInfo.getId();
        // 获取redis的hash操作对象
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
        for(Cart cart : cartList){
            // 查询是否已存在同一商品
            Long skuId = cart.getSkuId();
            Integer num = cart.getNum();
            if(hashOps.hasKey(skuId.toString())){
                // 存在,获取购物车数据
                String json = hashOps.get(skuId.toString());
                cart = JSONObject.parseObject(json, Cart.class);
                // 修改购物车数量
                cart.setNum(cart.getNum() + num);
            }else{
                // 不存在,新增购物车数据
                cart.setUserId(userInfo.getId());
                // 其它商品信息，需要查询商品服务
                Sku sku = goodsClient.querySkuById(skuId);
                cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
                cart.setPrice(sku.getPrice());
                cart.setTitle(sku.getTitle());
                cart.setOwnSpec(sku.getOwnSpec());
            }
            //存入redis
            hashOps.put(skuId.toString(),JSONObject.toJSONString(cart));
        }
    }

    public List<Cart> queryCartList() {
        // 获取登录用户
        UserInfo user = LoginInterceptor.getLoginUser();
        // 判断是否存在购物车
        String key = KEY_PREFIX + user.getId();
        if(!redisTemplate.hasKey(key)){
            // 不存在，直接返回
            throw new LyException(ExceptionEnum.CART_NOT_FOUND);
        }
        // 获取redis的hash操作对象
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
        List<Cart> carts = hashOps.values().stream()
                .map(s -> JSONObject.parseObject(s, Cart.class))
                .collect(Collectors.toList());
        return carts;
    }

    public void updateCarts(Cart cart) {
        // 获取登录用户
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + userInfo.getId();
        // 获取hash操作对象
        BoundHashOperations<String, String, String> hashOps = this.redisTemplate.boundHashOps(key);
        // 获取购物车信息
        String cartJson = hashOps.get(cart.getSkuId().toString());
        Cart cart1 = JSONObject.parseObject(cartJson, Cart.class);
        // 更新数量
        cart1.setNum(cart.getNum());
        // 写入购物车
        hashOps.put(cart.getSkuId().toString(), JSONObject.toJSONString(cart1));
    }

    public void deleteCart(String skuId) {
        // 获取登录用户
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + userInfo.getId();
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
        hashOps.delete(skuId);
    }
}
