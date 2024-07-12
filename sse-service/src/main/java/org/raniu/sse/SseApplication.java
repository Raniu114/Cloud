package org.raniu.sse;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan("org.raniu.common.**")
@ComponentScan("org.raniu.sse.**")
public class SseApplication{

    public static void main(String[] args) {
        SpringApplication.run(SseApplication.class, args);
    }
}