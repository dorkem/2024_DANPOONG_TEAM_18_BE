package com.memorytree.forest.controller;

import com.memorytree.forest.dto.global.ResponseDto;
import com.memorytree.forest.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vi/setting")
public class SettingController {

    private final UserService userService;

    public SettingController(UserService userService) {
        this.userService = userService;
    }

    // 사용자 설정 정보 조회
    @GetMapping
    public ResponseDto<Object> getSetting(@UserId Long kakaoId) {
        return userService.getUserSettings(kakaoId);
    }
}
