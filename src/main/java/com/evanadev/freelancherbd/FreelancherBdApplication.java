package com.evanadev.freelancherbd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class FreelancherBdApplication {
    public static void main(String[] args) {
        SpringApplication.run(FreelancherBdApplication.class, args);
        System.out.println("Application is started successfully");

    }

}
