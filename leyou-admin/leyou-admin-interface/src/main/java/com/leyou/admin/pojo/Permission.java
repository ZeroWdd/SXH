package com.leyou.admin.pojo;

import lombok.Data;

import javax.persistence.Table;

/**
 * @Auther: wdd
 * @Date: 2019/11/18 21:26
 * @Description:
 */
@Data
@Table(name = "tb_permission")
public class Permission {
    private Long permissionId;
    private String permissionName;
    private String url;
}
