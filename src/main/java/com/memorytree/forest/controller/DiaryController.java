package com.memorytree.forest.controller;

import com.memorytree.forest.annotation.UserId;
import com.memorytree.forest.dto.global.ResponseDto;
import com.memorytree.forest.dto.request.DiaryAudioRequestDto;
import com.memorytree.forest.dto.request.DiaryTextRequestDto;
import com.memorytree.forest.dto.response.DiaryAudioResponseDto;
import com.memorytree.forest.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vi/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PatchMapping("/audio")
    public ResponseDto<?> audio(
            @UserId Long userId,
            @RequestBody DiaryAudioRequestDto diaryAudioRequestDto
    ){
        DiaryAudioResponseDto diaryAudioResponseDto = diaryService.audioRecord(userId, diaryAudioRequestDto.type());
        return ResponseDto.ok(diaryAudioResponseDto);
    }
    @PatchMapping("/text")
    public ResponseDto<?> text(
            @UserId Long userId,
            @RequestBody DiaryTextRequestDto diaryTextRequestDto
    ){
        diaryService.textRecord(userId, diaryTextRequestDto);
        return ResponseDto.ok(null);
    }

}
