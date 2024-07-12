package org.raniu.android;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class AndroidUpdateApplication {
    public static void main(String[] args) {
        SpringApplication.run(AndroidUpdateApplication.class, args);
    }
}