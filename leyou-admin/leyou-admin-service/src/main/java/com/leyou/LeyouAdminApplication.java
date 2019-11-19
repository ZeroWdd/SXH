package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Auther: wdd
 * @Date: 2019/11/18 21:46
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.leyou.admin.mapper")
public class LeyouAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeyouAdminApplication.class,args);
    }
}
