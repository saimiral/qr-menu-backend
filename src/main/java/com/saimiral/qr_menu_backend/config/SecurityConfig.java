package com.saimiral.qr_menu_backend.config;

import com.saimiral.qr_menu_backend.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public - πελάτης
                        .requestMatchers("/api/menu/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/orders").permitAll()
                        // Public - auth
                        .requestMatchers("/api/auth/**").permitAll()
                        // Kitchen - μόνο με JWT
                        .requestMatchers("/api/orders/kitchen/**").hasAnyRole("KITCHEN", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/orders/*/status").hasAnyRole("KITCHEN", "ADMIN")
                        // Όλα τα υπόλοιπα
                        .anyRequest().hasRole("ADMIN")
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}