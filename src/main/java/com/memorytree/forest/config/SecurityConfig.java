package com.memorytree.forest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/login/**", "/favicon.ico").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2AuthenticationSuccessHandler())
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    private AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            var attributes = oAuth2User.getAttributes();
            var kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            var profile = (Map<String, Object>) kakaoAccount.get("profile");

            String email = (String) kakaoAccount.get("email");
            String nickname = (String) profile.get("nickname");
            String profileImage = (String) profile.get("profile_image_url");

            var responseDto = ResponseDto.ok(new LoginResponseDto(email, nickname, profileImage));
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            new ObjectMapper().writeValue(response.getWriter(), responseDto);
        };
    }

    public record LoginResponseDto(String email, String nickname, String profileImage) {}

    public record ResponseDto<T>(boolean success, T data, String error) {
        public static <T> ResponseDto<T> ok(T data) {
            return new ResponseDto<>(true, data, null);
        }
    }
}

