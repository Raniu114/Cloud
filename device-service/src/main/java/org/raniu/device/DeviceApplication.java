package org.raniu.device;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
@MapperScan("org.raniu.device.mapper")
public class DeviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeviceApplication.class, args);
    }
}
