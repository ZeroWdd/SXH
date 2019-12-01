package com.leyou.admin.config;

import com.leyou.common.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtProperties jwtProperties;

//    @Bean
//    public CheckInterceptor checkInterceptor() {
//        return new CheckInterceptor(jwtProperties);
//    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(checkInterceptor())
//                .addPathPatterns("/**").excludePathPatterns("/admin/query");
//    }
}
