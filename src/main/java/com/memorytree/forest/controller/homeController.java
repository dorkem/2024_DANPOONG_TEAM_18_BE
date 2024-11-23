package com.memorytree.forest.controller;

import com.memorytree.forest.annotation.UserId;
import com.memorytree.forest.dto.global.ResponseDto;
import com.memorytree.forest.dto.response.HomeResponceDto;
import com.memorytree.forest.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vi/home")
public class homeController {
    private final UserService userService;

    public homeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    private ResponseDto<?> home (
            @UserId Long id
    ){
        HomeResponceDto homeResponceDto = userService.returnLoginStreaks(id);
        return ResponseDto.ok(homeResponceDto);
    }
}
