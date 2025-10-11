package com.evanadev.freelancherbd.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class AuditorConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            // For using Spring Security:
             Authentication auth = SecurityContextHolder.getContext().getAuthentication();
             return Optional.ofNullable(auth != null ? auth.getName() : "system");

            // Without Spring Security:
            //return Optional.of("system");
        };
    }
}
