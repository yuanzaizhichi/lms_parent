package com.cxf.community;

import com.cxf.common.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

//配置springboot的包扫描
@SpringBootApplication(scanBasePackages = "com.cxf")
//配置JPA注解的扫描
@EntityScan(value = "com.cxf")
public class CommunityApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker();
    }
}
