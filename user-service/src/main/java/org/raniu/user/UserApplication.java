package org.raniu.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
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

@SpringBootApplication()
@MapperScan("org.raniu.user.mapper")
@ComponentScan("org.raniu.common.**")
@ComponentScan("org.raniu.user.**")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
