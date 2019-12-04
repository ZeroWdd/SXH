package com.leyou.seckill.service;

import com.leyou.seckill.mapper.SeckillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: wdd
 * @Date: 2019/12/4 18:40
 * @Description:
 */
@Service
public class SeckillService {
    @Autowired
    private SeckillMapper seckillMapper;
}
