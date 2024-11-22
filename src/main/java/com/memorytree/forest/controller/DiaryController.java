package com.memorytree.forest.controller;

import com.memorytree.forest.annotation.UserId;
import com.memorytree.forest.dto.global.ResponseDto;
import com.memorytree.forest.dto.request.DiaryAudioRequestDto;
import com.memorytree.forest.dto.request.DiaryTextRequestDto;
import com.memorytree.forest.dto.response.DiaryAudioResponseDto;
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

    @PatchMapping("/audio")
    public ResponseDto<?> audio(
            @UserId Long id,
            @RequestBody DiaryAudioRequestDto diaryAudioRequestDto
    ){
        DiaryAudioResponseDto diaryAudioResponseDto = diaryService.audioRecord(id, diaryAudioRequestDto.type());
        return ResponseDto.ok(diaryAudioResponseDto);
    }
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
    @PatchMapping("/quiz/answer")
    public ResponseDto<?> quizAnswer(
            @UserId Long id,
            @RequestBody String choice
    ){
        DiaryQuizAnswerResponseDto diaryQuizAnswerResponseDto = diaryService.confirmAnswer(id,choice);
        return ResponseDto.ok(diaryQuizAnswerResponseDto);
    }

}
