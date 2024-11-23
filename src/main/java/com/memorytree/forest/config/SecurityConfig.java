package com.memorytree.forest.config;

import com.memorytree.forest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {
    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/login/**", "/favicon.ico", "/api/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    public record LoginResponseDto(Long id, String email, String nickname, String profileImage) {}

    public record ResponseDto<T>(boolean success, T data, String error) {
        public static <T> ResponseDto<T> ok(T data) {
            return new ResponseDto<>(true, data, null);
        }
    }
}

