package com.memorytree.forest.controller;

import com.memorytree.forest.annotation.UserId;
import com.memorytree.forest.dto.global.ResponseDto;
import com.memorytree.forest.dto.request.DiaryTextRequestDto;
import com.memorytree.forest.dto.response.DiaryQuizAnswerResponseDto;
import com.memorytree.forest.dto.response.DiaryQuizResponseDto;
import com.memorytree.forest.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/vi/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/text")
    public ResponseDto<?> text(
            @UserId Long id,
            @RequestBody DiaryTextRequestDto diaryTextRequestDto
    ){
        diaryService.textRecord(id, diaryTextRequestDto);
        return ResponseDto.ok(null);
    }
    @GetMapping("/quiz")
    public ResponseDto<?> quiz(
            @UserId Long id
    ){
        DiaryQuizResponseDto diaryQuizResponseDto = diaryService.createQuiz(id);
        return ResponseDto.ok(diaryQuizResponseDto);
    }

}
