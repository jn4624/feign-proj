package com.app.feign.config;

import com.app.feign.feign.logger.FeignCustomLogger;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public Logger feignLogger() {
        return new FeignCustomLogger();
    }
}
