package com.evanadev.freelancherbd.configuration;

import com.evanadev.freelancherbd.service.CustomUserDetails;
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

    private final CustomUserDetails customUserDetails;

    public SecurityConfiguration(CustomUserDetails customUserDetails) {
        this.customUserDetails = customUserDetails;
    }
@Bean
public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(request -> request.requestMatchers("/css/**", "/js/**", "/images/**", "/assets/**", "/webjars/**").permitAll()
            .requestMatchers("/register", "/login").permitAll()
            .anyRequest().authenticated())
            .formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/home", true).permitAll())
            .logout(logout -> logout.logoutSuccessUrl("/login").permitAll()).userDetailsService(customUserDetails);
    return http.build();
}

@Bean
public PasswordEncoder passwordEncoder()    {
return new BCryptPasswordEncoder();
}
}
