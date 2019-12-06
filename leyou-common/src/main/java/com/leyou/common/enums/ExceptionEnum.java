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
    USER_NOT_FOUND(404,"用户未查到"),
    CATEGORY_NOT_FOUND(404,"分类未查到"),
    SPEC_GROUP_NOT_FOUND(404,"商品规格组未查到"),
    SPEC_PARAM_NOT_FOUND(404,"商品规格参数未查到"),
    GOODS_NOT_FOUND(404,"商品未查到"),
    GOODS_DETAIL_NOT_FOUND(404,"商品SPU_DETAIL未查到"),
    ORDER_STATUS_NOT_FOUND(404,"订单状态未查到"),
    GOODS_SKU_NOT_FOUND(404,"商品SKU未查到"),
    STOCK_NOT_FOUND(404,"库存未查到"),
    CART_NOT_FOUND(404,"购物车为空"),
    ADDRESS_NOT_FOUND(404,"地址未查询到"),
    ORDER_NOT_FOUND(404,"订单未查询到"),
    ADMIN_NOT_FOUND(404,"管理员未查询到"),
    ROLE_NOT_FOUND(404,"角色未查询到"),
    PERMISSION_NOT_FOUND(404,"权限未查询到"),
    SECKILL_NOT_FOUND(404,"秒杀未查询到"),

    BRAND_SAVE_ERROR(500,"品牌新增失败"),
    BRAND_UPDATE_ERROR(500,"品牌修改失败"),
    GOODS_SAVE_ERROR(500,"商品新增失败"),
    GOODS_UPDATE_ERROR(500,"商品修改失败"),
    PHONE_SEND_ERROR(500,"短信发送失败"),
    USER_SAVE_ERROR(500,"用户注册失败"),
    ADDRESS_SAVE_ERROR(500,"地址新增失败"),
    ADDRESS_UPDATE_ERROR(500,"地址修改失败"),
    ADDRESS_DELETE_ERROR(500,"地址删除失败"),
    ORDER_SAVE_ERROR(500,"订单添加失败"),
    ORDER_UPDATE_ERROR(500,"订单修改失败"),
    STOCK_DECREASE_ERROR(500,"库存减少失败"),
    STOCK_LACK_ERROR(500,"库存不足"),
    SPEC_GROUP_SAVE_ERROR(500,"规格组新增失败"),
    SPEC_GROUP_DELETE_ERROR(500,"规格组删除失败"),
    SPEC_GROUP_UPDATE_ERROR(500,"规格组修改失败"),
    SPEC_PARAM_SAVE_ERROR(500,"规格参数新增失败"),
    SPEC_PARAM_UPDATE_ERROR(500,"规格参数修改失败"),
    SPEC_PARAM_DELETE_ERROR(500,"规格参数删除失败"),
    USER_DELETE_ERROR(500,"用户删除失败"),
    ADMIN_SAVE_ERROR(500,"管理员添加失败"),
    ADMIN_DELETE_ERROR(500,"管理员删除失败"),
    ADMIN_UPDATE_ERROR(500,"管理员修改失败"),
    ROLE_SAVE_ERROR(500,"角色保存失败"),
    ROLE_UPDATE_ERROR(500,"角色修改失败"),
    ROLE_DELETE_ERROR(500,"角色删除失败"),
    ADMIN_DEAL_ROLE_ERROR(500,"管理员分配角色失败"),
    PERMISSION_SAVE_ERROR(500,"权限添加失败"),
    PERMISSION_DELETE_ERROR(500,"权限删除失败"),
    PERMISSION_UPDATE_ERROR(500,"权限更改失败"),
    ORDER_QUERY_ERROR(500,"订单查询异常"),
    PASSWORD_ERROR(500,"密码错误"),
    USER_UPDATE_ERROR(500,"用户更新失败"),
    ORDER_DELETE_ERROR(500,"订单删除失败"),
    SECKILL_SAVE_ERROR(500,"秒杀存储失败"),
    SECKILL_UPDATE_ERROR(500,"秒杀修改失败"),
    STOCK_UPDATE_ERROR(500,"库存修改失败"),

    CATEGORY_CHILD_NODE(403,"分类存在子节点"),
    STATUS_is_true(403,"状态已更改，无需再次操作"),

    INVALID_USER_TYPE(400,"无效的参数"),
    INVALID_VERIFY_CODE(400,"无效的验证码"),
    USERNAME_OR_PASSWORD_ERROR(400,"用户名或密码错误"),
    INVALID_TOKEN(400,"无效的用户凭证"),
    INVALID_ORDER_TYPE(400,"无效的订单状态"),
    INVALID_DATE(400,"无效的日期"),
    INVALID_STOCK_NUM(400,"无效的库存数量"),
    ;
    private int code;
    private String msg;
}
