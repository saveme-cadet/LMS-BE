package com.larry.fc.finalproject.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = "com.larry.fc.finalproject")
@EntityScan("com.larry.fc.finalproject.core")
@EnableJpaRepositories("com.larry.fc.finalproject.core")
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}

