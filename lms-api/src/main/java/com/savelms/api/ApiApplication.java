package com.savelms.api;

import com.savelms.config.data_loader.UserDataLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = "com.savelms")
@EnableJpaRepositories(basePackages = "com.savelms.core")
@EntityScan("com.savelms")
@EnableJpaAuditing
@Import(UserDataLoader.class)
//@ComponentScan("com.savelms")
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}

