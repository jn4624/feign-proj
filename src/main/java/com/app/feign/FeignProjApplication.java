package com.app.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class FeignProjApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignProjApplication.class, args);
    }

}
