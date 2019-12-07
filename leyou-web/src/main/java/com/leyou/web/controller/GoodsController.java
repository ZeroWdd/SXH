package com.leyou.web.controller;

import com.leyou.web.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2019/11/4 16:58
 * @Description:
 */
@Controller
@RequestMapping("/item")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 跳转到商品详情页
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/{id}.html")
    public String toItemPage(Model model, @PathVariable("id")Long id){
        // 加载所需的数据
        Map<String, Object> modelMap = this.goodsService.loadData(id);
        // 放入模型
        model.addAllAttributes(modelMap);
        return "item";
    }

    /**
     * 跳转到详情商品页
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/seckill/{id}.html")
    public String toSeckillPage(Model model, @PathVariable("id")Long id){
        // 加载所需的数据
        Map<String, Object> modelMap = this.goodsService.seckillData(id);
        // 放入模型
        model.addAllAttributes(modelMap);
        return "seckillItem";
    }
}
