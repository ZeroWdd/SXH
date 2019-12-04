package com.leyou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Auther: wdd
 * @Date: 2019/12/4 17:29
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.leyou.seckill.mapper")
public class LeyouSeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeyouSeckillApplication.class, args);
    }
}
