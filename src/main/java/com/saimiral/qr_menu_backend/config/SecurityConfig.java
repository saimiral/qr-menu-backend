package com.saimiral.qr_menu_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public - ο πελάτης δεν χρειάζεται auth
                        .requestMatchers("/api/menu/**").permitAll()
                        .requestMatchers("/api/orders").permitAll()
                        // Όλα τα υπόλοιπα προς το παρόν τα αφήνουμε ανοιχτά μέχρι να φτιάξουμε JWT
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}