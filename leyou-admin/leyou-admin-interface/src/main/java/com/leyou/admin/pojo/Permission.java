package com.leyou.admin.pojo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @Auther: wdd
 * @Date: 2019/11/18 21:26
 * @Description:
 */
@Data
@Table(name = "tb_permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;
    @Length(min = 2, max = 10, message = "权限名只能在2~10位之间")
    private String permissionName;
    @NotNull(message = "地址不能为空")
    private String url;
}
