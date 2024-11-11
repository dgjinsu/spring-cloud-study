package com.example.todoappserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {"com.example.todoappserver", "com.example.common"})
public class TodoAppServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoAppServerApplication.class, args);
    }

}
