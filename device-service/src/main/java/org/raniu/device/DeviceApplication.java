package org.raniu.device;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.gateway
 * @className: GatewayApplication
 * @author: Raniu
 * @description: TODO
 * @date: 2024/4/26 16:06
 * @version: 1.0
 */
@EnableFeignClients("org.raniu.api.client")
@EnableDiscoveryClient
@SpringBootApplication()
@MapperScan("org.raniu.device.mapper")
@ComponentScan("org.raniu.common.**")
@ComponentScan("org.raniu.device.**")
@ComponentScan("org.raniu.api.**")
public class DeviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeviceApplication.class, args);
    }
}
