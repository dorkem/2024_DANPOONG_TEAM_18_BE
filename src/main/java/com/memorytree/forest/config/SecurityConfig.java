package com.memorytree.forest.config;

import com.memorytree.forest.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

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
                        .requestMatchers("/api/auth/login/**", "/favicon.ico", "/api/v1/setting").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
//
//    private AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
//        return (request, response, authentication) -> {
//
//            log.info("d");
//
//            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//            var attributes = oAuth2User.getAttributes();
//            var kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
//            var profile = (Map<String, Object>) kakaoAccount.get("profile");
//
//            Long id = (Long) attributes.get("id");
//            String email = (String) kakaoAccount.get("email");
//            String nickname = (String) profile.get("nickname");
//            String profileImage = (String) profile.get("profile_image_url");
//
//            userService.createUser(id, nickname);
//            userService.accumulateLoginStreak(id);
//
//            var responseDto = ResponseDto.ok(new LoginResponseDto(id, email, nickname, profileImage));
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            new ObjectMapper().writeValue(response.getWriter(), responseDto);
//        };
//    }

    public record LoginResponseDto(Long id, String email, String nickname, String profileImage) {}

    public record ResponseDto<T>(boolean success, T data, String error) {
        public static <T> ResponseDto<T> ok(T data) {
            return new ResponseDto<>(true, data, null);
        }
    }
}

