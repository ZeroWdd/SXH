package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Auther: wdd
 * @Date: 2019/11/16 17:24
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.leyou.stock.mapper")
public class LeyouStockApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeyouStockApplication.class,args);
    }
}
