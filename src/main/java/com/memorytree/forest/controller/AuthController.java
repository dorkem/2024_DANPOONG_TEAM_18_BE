package com.memorytree.forest.controller;

import com.memorytree.forest.dto.global.ResponseDto;
import com.memorytree.forest.dto.response.LoginResponseDto;
import com.memorytree.forest.service.KakaoAuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final KakaoAuthService kakaoAuthService;

    public AuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @GetMapping("/login/kakao")
    public ResponseDto<LoginResponseDto> loginWithKakao(@RequestParam String code) {
        return kakaoAuthService.getKakaoUserInfo(code);
    }
}

