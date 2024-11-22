package com.memorytree.forest.service;

import com.memorytree.forest.dto.response.KakaoUserInfoResponseDto;
import com.memorytree.forest.domain.User;
import com.memorytree.forest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    public KakaoUserInfoResponseDto getKakaoUserInfo(String code) {
        // 1. 인가 코드로 액세스 토큰 요청
        String accessToken = getAccessToken(code);

        // 2. 액세스 토큰으로 사용자 정보 요청
        KakaoUserInfoResponseDto userInfo = getUserInfoFromKakao(accessToken);

        // 3. 사용자 정보를 User 테이블에 저장
        saveUser(userInfo);

        return userInfo;
    }

    private String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "YOUR_KAKAO_CLIENT_ID");
        body.add("redirect_uri", "http://localhost:8080/login/oauth2/code/kakao");
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(KAKAO_TOKEN_URL, request, Map.class);

        return (String) response.getBody().get("access_token");
    }

    private KakaoUserInfoResponseDto getUserInfoFromKakao(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                KAKAO_USER_INFO_URL,
                HttpMethod.GET,
                request,
                Map.class
        );

        Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
        Map<String, Object> properties = (Map<String, Object>) response.getBody().get("properties");

        return KakaoUserInfoResponseDto.builder()
                .id((Long) response.getBody().get("id"))
                .email((String) kakaoAccount.get("email"))
                .nickname((String) properties.get("nickname"))
                .profileImage((String) properties.get("profile_image"))
                .build();
    }

    private void saveUser(KakaoUserInfoResponseDto userInfo) {
        // 이미 존재하는 사용자 확인
        User existingUser = userRepository.findById(userInfo.getId()).orElse(null);

        if (existingUser == null) {
            User newUser = User.builder()
                    .id(userInfo.getId())
                    .name(userInfo.getNickname())
                    .profile(userInfo.getProfileImage())
                    .build();
            userRepository.save(newUser);
        }
    }
}
