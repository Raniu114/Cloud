package org.raniu.configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients("org.raniu.api.client")
@EnableDiscoveryClient
@SpringBootApplication()
@MapperScan("org.raniu.configuration.mapper")
@ComponentScan("org.raniu.common.**")
@ComponentScan("org.raniu.configuration.**")
@ComponentScan("org.raniu.api.**")
public class ConfigurationApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigurationApplication.class, args);
    }
}