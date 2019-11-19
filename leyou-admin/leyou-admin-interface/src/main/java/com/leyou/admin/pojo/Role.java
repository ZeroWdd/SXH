package com.leyou.admin.pojo;

import lombok.Data;

import javax.persistence.Table;

/**
 * @Auther: wdd
 * @Date: 2019/11/18 21:26
 * @Description:
 */
@Data
@Table(name = "tb_role")
public class Role {
    private Long roleId;
    private String roleName;
}
