package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Auther: wdd
 * @Date: 2019/11/4 16:47
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LeyouWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeyouWebApplication.class, args);
    }
}
