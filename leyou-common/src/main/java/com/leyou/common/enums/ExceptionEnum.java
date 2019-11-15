package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Auther: wdd
 * @Date: 2019/10/17 19:10
 * @Description:
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    BRAND_NOT_FOUND(404,"品牌未查到"),
    CATEGORY_NOT_FOUND(404,"分类未查到"),
    SPEC_GROUP_NOT_FOUND(404,"商品规格组未查到"),
    SPEC_PARAM_NOT_FOUND(404,"商品规格参数未查到"),
    GOODS_NOT_FOUND(404,"商品未查到"),
    GOODS_DETAIL_NOT_FOUND(404,"商品SPU_DETAIL未查到"),
    GOODS_SKU_NOT_FOUND(404,"商品SKU未查到"),
    STOCK_NOT_FOUND(404,"库存未查到"),
    CART_NOT_FOUND(404,"购物车为空"),
    ADDRESS_NOT_FOUND(404,"地址为查询到"),

    BRAND_SAVE_ERROR(500,"品牌新增失败"),
    BRAND_UPDATE_ERROR(500,"品牌修改失败"),
    GOODS_SAVE_ERROR(500,"商品新增失败"),
    GOODS_UPDATE_ERROR(500,"商品修改失败"),
    PHONE_SEND_ERROR(500,"短信发送失败"),
    USER_SAVE_ERROR(500,"用户注册失败"),
    ADDRESS_SAVE_ERROR(500,"地址新增失败"),
    ADDRESS_UPDATE_ERROR(500,"地址修改失败"),
    ADDRESS_DELETE_ERROR(500,"地址删除失败"),

    CATEGORY_CHILD_NODE(403,"分类存在子节点"),

    INVALID_USER_TYPE(400,"无效的参数"),
    INVALID_VERIFY_CODE(400,"无效的验证码"),
    USERNAME_OR_PASSWORD_ERROR(400,"用户名或密码错误"),
    INVALID_TOKEN(400,"无效的用户凭证"),
    ;
    private int code;
    private String msg;
}
