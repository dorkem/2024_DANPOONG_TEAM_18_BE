package com.memorytree.forest.controller;

import com.memorytree.forest.annotation.UserId;
import com.memorytree.forest.dto.global.ResponseDto;
import com.memorytree.forest.dto.request.GameResultRequestDto;
import com.memorytree.forest.dto.response.GameRecordDto;
import com.memorytree.forest.dto.response.GameResultResponseDto;
import com.memorytree.forest.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseDto<GameRecordDto> getTodayGame(@UserId Long id) {
        return ResponseDto.ok(gameService.generateRandomGameRecordDto(id));
    }

    @PatchMapping("/play")
    public ResponseDto<GameResultResponseDto> updateHighScore(@UserId Long id, @RequestBody GameResultRequestDto gameResult) {
        return ResponseDto.ok(gameService.updateHighScore(id, gameResult));
    }
}
