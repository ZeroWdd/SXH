package com.leyou.common.pojo;

import lombok.Data;

import java.util.List;

/**
 * @Classname PageResult
 * @Description None
 * @Date 2019/8/4 15:54
 * @Created by WDD
 */
@Data
public class PageResult<T> {
    private Long total;// 总条数
    private Integer totalPage;// 总页数
    private List<T> items;// 当前页数据


    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total, Integer totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }

    public PageResult() {

    }
}
