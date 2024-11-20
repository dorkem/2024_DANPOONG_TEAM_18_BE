package com.memorytree.forest.service;

import com.memorytree.forest.domain.Diary;
import com.memorytree.forest.domain.User;
import com.memorytree.forest.dto.request.DiaryTextRequestDto;
import com.memorytree.forest.dto.response.DiaryAudioResponseDto;
import com.memorytree.forest.exception.CommonException;
import com.memorytree.forest.exception.ErrorCode;
import com.memorytree.forest.repository.DiaryRepository;
import com.memorytree.forest.repository.UserRepository;
import com.memorytree.forest.stt.SttService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class DiaryService {
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final SttService sttService;

    public  DiaryService(UserRepository userRepository,DiaryRepository diaryRepository ,SttService sttService){
        this.userRepository = userRepository;
        this.diaryRepository = diaryRepository;
        this.sttService = sttService;
    }

    public DiaryAudioResponseDto audioRecord(Long id,String type){
        // 사용자 정보 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.WRONG_USER));

        // 오늘 날짜
        LocalDate today = LocalDate.now();

        // Diary 객체 조회
        Diary diary = diaryRepository.findByWhenAndUser(today, user)
                .orElseGet(() -> {
                    // Diary가 없으면 생성
                    Diary newDiary = new Diary(user);
                    return diaryRepository.save(newDiary);
                });

        // 음성인식 수행
        String content = sttService.recognizeSpeechFor3Seconds();

        //음성 인식 결과가 null인 경우 예외 발생
        if (content == null || content.isBlank()) {
            throw new CommonException(ErrorCode.STT_NO_SPEECH);
        }

        switch (type.toLowerCase()) {
            case "who":
                diary.updateWho(content);
                break;
            case "what":
                diary.updateWhat(content);
                break;
            case "where":
                diary.updateWhere(content);
                break;
            default:
                throw new CommonException(ErrorCode.BAD_REQUEST_JSON);
        }
        diaryRepository.save(diary);
        return new DiaryAudioResponseDto(type, content);
    }


    public void textRecord(Long id, DiaryTextRequestDto diaryTextRequestDto){
        // 사용자 정보 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.WRONG_USER));
        Diary newDiary = new Diary(user);
        newDiary.updateWhere(diaryTextRequestDto.diary_where());
        newDiary.updateWho(diaryTextRequestDto.diary_who());
        newDiary.updateWhat(diaryTextRequestDto.diary_what());
        return;
    }
}
