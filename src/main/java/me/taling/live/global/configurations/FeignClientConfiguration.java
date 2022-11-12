package me.taling.live.global.configurations;

import feign.Logger;
import feign.Request;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@EnableFeignClients(basePackages = "me.taling.live.infra.*")
@Configuration
public class FeignClientConfiguration {
    @Bean
    public Logger.Level commonFeignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Request.Options commonFeignOption() {
        return new Request.Options(10L, TimeUnit.SECONDS, 5L, TimeUnit.SECONDS, false);
    }
}
