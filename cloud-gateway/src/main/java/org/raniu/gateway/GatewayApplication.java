package org.raniu.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.gateway
 * @className: GatewayApplication
 * @author: Raniu
 * @description: TODO
 * @date: 2024/4/26 16:06
 * @version: 1.0
 */

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
