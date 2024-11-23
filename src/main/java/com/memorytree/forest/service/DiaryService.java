package com.memorytree.forest.service;

import com.memorytree.forest.domain.Diary;
import com.memorytree.forest.domain.User;
import com.memorytree.forest.dto.request.DiaryTextRequestDto;
import com.memorytree.forest.dto.response.DiaryQuizAnswerResponseDto;
import com.memorytree.forest.dto.response.DiaryQuizResponseDto;
import com.memorytree.forest.exception.CommonException;
import com.memorytree.forest.exception.ErrorCode;
import com.memorytree.forest.repository.DiaryRepository;
import com.memorytree.forest.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class DiaryService {
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final UserService userService;

    public  DiaryService(UserRepository userRepository, DiaryRepository diaryRepository, UserService userService){
        this.userRepository = userRepository;
        this.diaryRepository = diaryRepository;
        this.userService = userService;
    }


    public void textRecord(Long id, DiaryTextRequestDto diaryTextRequestDto){
        // 요청 파라미터 검증
        if (id == null || diaryTextRequestDto == null) {
            throw new CommonException(ErrorCode.MISSING_REQUEST_PARAMETER);
        }
        // 사용자 정보 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.WRONG_USER));
        // 오늘 날짜 조회
        LocalDate today = LocalDate.now();

        // 오늘 날짜와 해당 사용자로 Diary 조회
        boolean diaryExists = diaryRepository.findByWhenAndUser(today, user).isPresent();
        if (diaryExists) {
            // Diary가 이미 존재하면 예외 발생
            throw new CommonException(ErrorCode.DIARY_ALREADY_EXISTS);
        }

        // Diary 생성 및 업데이트
        Diary newDiary = new Diary(user);
        if (diaryTextRequestDto.diary_where() == null || diaryTextRequestDto.diary_who() == null || diaryTextRequestDto.diary_what() == null) {
            throw new CommonException(ErrorCode.MISSING_REQUEST_PARAMETER);
        }
        newDiary.updateWhere(diaryTextRequestDto.diary_where());
        newDiary.updateWho(diaryTextRequestDto.diary_who());
        newDiary.updateWhat(diaryTextRequestDto.diary_what());
        diaryRepository.save(newDiary);
        userService.addEXPAfterDiaryWrote(id);
    }

    public DiaryQuizResponseDto createQuiz(Long id){
        // 요청 파라미터 검증
        if (id == null) {
            throw new CommonException(ErrorCode.MISSING_REQUEST_PARAMETER);
        }

        // 사용자 정보 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.WRONG_USER));

        // 사용자의 모든 Diary 조회
        List<Diary> userDiaries = diaryRepository.findAllByUserId(id);

        // Diary가 없으면 NOT_FOUND_DIARY 예외 발생
        if (userDiaries.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_DIARY);
        }
        // 랜덤으로 질문 필드 선택
        List<String> fields = Arrays.asList("when", "what", "where", "who");
        Random random = new Random();
        String questionField = fields.get(random.nextInt(fields.size()));

        // 질문을 위한 Diary 객체와 값 설정
        Diary targetDiary = userDiaries.get(0); // 기준 Diary
        String correctAnswer;

        switch (questionField) {
            case "when":
                correctAnswer = targetDiary.getWhen().toString();
                break;
            case "what":
                correctAnswer = targetDiary.getWhat();
                break;
            case "where":
                correctAnswer = targetDiary.getWhere();
                break;
            case "who":
                correctAnswer = targetDiary.getWho();
                break;
            default:
                throw new CommonException(ErrorCode.BAD_REQUEST_JSON);
        }
        // 보기 생성
        List<String> choices = new ArrayList<>();
        choices.add(correctAnswer);
        user.updateDiaryAnswer(correctAnswer);
        // 사용자의 다른 Diary에서 보기 값 가져오기
        for (Diary diary : userDiaries) {
            if (choices.size() == 4) break; // 최대 4개 보기
            String value;
            switch (questionField) {
                case "when":
                    value = diary.getWhen().toString();
                    break;
                case "what":
                    value = diary.getWhat();
                    break;
                case "where":
                    value = diary.getWhere();
                    break;
                case "who":
                    value = diary.getWho();
                    break;
                default:
                    continue;
            }
            if (!choices.contains(value)) {
                choices.add(value);
            }
        }
        // 보기 수가 4개 미만이면 NOT_ENOUGH_DIARY 예외 발생
        if (choices.size() < 4) {
            throw new CommonException(ErrorCode.NOT_ENOUGH_DIARY);
        }
        // 보기 순서 섞기
        Collections.shuffle(choices);

        return new DiaryQuizResponseDto(generateQuestionText(questionField, targetDiary, correctAnswer), choices);
    }
    // 질문 텍스트 생성
    private String generateQuestionText(String field, Diary diary, String correctAnswer) {
        String when = diary.getWhen().toString();
        String what = diary.getWhat();
        String where = diary.getWhere();
        String who = diary.getWho();

        // correctAnswer를 제외한 나머지 필드의 값
        List<String> otherFields = new ArrayList<>();
        if (!"when".equals(field)) {
            otherFields.add("언제: " + when);
        }
        if (!"what".equals(field)) {
            otherFields.add("무엇: " + what);
        }
        if (!"where".equals(field)) {
            otherFields.add("어디: " + where);
        }
        if (!"who".equals(field)) {
            otherFields.add("누구: " + who);
        }

        // 나머지 필드 값들을 쉼표로 연결
        String otherFieldsInfo = String.join(", ", otherFields);

        // 질문 텍스트 생성
        switch (field) {
            case "when":
                return "언제의 일인가요? (" + otherFieldsInfo + ")";
            case "what":
                return "무엇을 했나요? (" + otherFieldsInfo + ")";
            case "where":
                return "어디를 갔나요? (" + otherFieldsInfo + ")";
            case "who":
                return "누구와 함께 있었나요? (" + otherFieldsInfo + ")";
            default:
                throw new CommonException(ErrorCode.BAD_REQUEST_JSON);
        }
    }

    public DiaryQuizAnswerResponseDto confirmAnswer(Long id, String answer){
        // 요청 파라미터 검증
        if (id == null || answer == null) {
            throw new CommonException(ErrorCode.MISSING_REQUEST_PARAMETER);
        }

        // 사용자 정보 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.WRONG_USER));

        boolean correct = false;
        if(user.getDiaryAnswer() == answer){
            correct = true;
        }
        userService.addEXPAfterQuizPlayed(id);
        return new DiaryQuizAnswerResponseDto(correct, user.getDiaryAnswer());
    }
}
