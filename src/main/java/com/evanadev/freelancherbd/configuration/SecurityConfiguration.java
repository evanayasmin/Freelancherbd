package com.evanadev.freelancherbd.configuration;

import com.evanadev.freelancherbd.service.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CustomUserDetailService customUserDetails;

    public SecurityConfiguration(CustomUserDetailService customUserDetails) {
        this.customUserDetails = customUserDetails;
    }
@Bean
public SecurityFilterChain configure(HttpSecurity http) throws Exception {

    http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                    // Static resources
                    .requestMatchers("/css/**", "/js/**", "/images/**", "/assets/**", "/webjars/**").permitAll()

                    // Auth pages
                    .requestMatchers("/login", "/register").permitAll()

                    // WebSocket handshake ONLY
                    .requestMatchers("/ws/**").permitAll().requestMatchers("/app/**", "/topic/**",     // Broadcast topics
                            "/user/**").permitAll()

                    // Public pages
                    .requestMatchers("/jobs/category/**", "/jobs/job_detail/**").permitAll()

                    // Role-based
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/employer/**").hasRole("EMPLOYER")
                    .requestMatchers("/freelancer/**").hasRole("FREELANCER")

                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/home", true)
                    .permitAll()
            )
            .logout(logout -> logout
                    .logoutSuccessUrl("/login")
                    .permitAll()
            )
            .userDetailsService(customUserDetails);

    return http.build();
}


@Bean
public PasswordEncoder passwordEncoder()    {
return new BCryptPasswordEncoder();
}
}
