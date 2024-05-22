package org.raniu.user;

import org.mybatis.spring.annotation.MapperScan;
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

@SpringBootApplication()
@MapperScan("org.raniu.user.mapper")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
