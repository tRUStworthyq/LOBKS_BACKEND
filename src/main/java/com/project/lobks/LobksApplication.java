package com.project.lobks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class LobksApplication {

    public static void main(String[] args) {
        SpringApplication.run(LobksApplication.class, args);
    }
}
