package com.memorytree.forest.service;

import com.memorytree.forest.dto.global.ResponseDto;
import com.memorytree.forest.dto.response.LoginResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoAuthService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    public ResponseDto<LoginResponseDto> getKakaoUserInfo(String code) {
        String accessToken = getAccessToken(code);
        LoginResponseDto userInfo = getUserInfo(accessToken);
        return ResponseDto.ok(userInfo);
    }

    private String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("client_id", clientId);
        params.put("redirect_uri", redirectUri);
        params.put("code", code);
        params.put("client_secret", clientSecret);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        return (String) response.getBody().get("access_token");
    }

    private LoginResponseDto getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);
        Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return new LoginResponseDto(
                (Long) response.getBody().get("id"),
                (String) profile.get("nickname"),
                (String) kakaoAccount.get("email"),
                (String) profile.get("profile_image_url")
        );
    }
}
