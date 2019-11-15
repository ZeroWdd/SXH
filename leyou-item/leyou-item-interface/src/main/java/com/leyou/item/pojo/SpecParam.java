package com.leyou.item.pojo;

import lombok.Data;

import javax.persistence.*;

/**
 * @Auther: wdd
 * @Date: 2019/10/23 21:11
 * @Description:
 */
@Data
@Table(name = "tb_spec_param")
public class SpecParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cid;
    private Long groupId;
    private String name;
    @Column(name = "`numeric`")
    private Boolean numeric;
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;

}