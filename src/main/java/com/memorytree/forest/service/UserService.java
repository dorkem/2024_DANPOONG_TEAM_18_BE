package com.memorytree.forest.service;

import com.memorytree.forest.domain.Game;
import com.memorytree.forest.domain.User;
import com.memorytree.forest.dto.global.ResponseDto;
import com.memorytree.forest.dto.response.HomeResponceDto;
import com.memorytree.forest.exception.CommonException;
import com.memorytree.forest.exception.ErrorCode;
import com.memorytree.forest.repository.GameRepository;
import com.memorytree.forest.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import java.time.LocalDate;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    public UserService(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    // 카카오 로그인 후 유저 생성
    public void createUser(Long kakaoId, String name) {
        Optional<User> existingUser = userRepository.findById(kakaoId);

        if (existingUser.isEmpty()) {
            User user = User.builder()
                    .id(kakaoId)
                    .name(name)
                    .build();
            userRepository.save(user);
            // Game 엔티티 생성
            Game game = Game.builder()
                    .user(user)
                    .numberSequenceGame(Float.MAX_VALUE)
                    .spotDifferenceGame(Float.MAX_VALUE)
                    .flipCardGame(Float.MAX_VALUE)
                    .tangramPuzzle(Float.MAX_VALUE)
                    .build();
            gameRepository.save(game);
        }

    }

    // 사용자 설정 정보 반환
    public ResponseDto<Object> getUserSettings(Long kakaoId) {
        Optional<User> user = userRepository.findById(kakaoId);
        if (user.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }

        User foundUser = user.get();
        Map<String, Object> response = new HashMap<>();
        response.put("username", foundUser.getName());
        response.put("coin_balance", foundUser.getCoin());

        return ResponseDto.ok(response);
    }

    public void accumulateLoginStreak(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CommonException(ErrorCode.WRONG_USER));

        LocalDate lastLoginDate = user.getLastLoginDate();
        LocalDate today = LocalDate.now();
        user.setLastLoginDate(today);
        Long daysBetween = ChronoUnit.DAYS.between(lastLoginDate, today);
        if (daysBetween > 1) {
            // 연속 출석일수 초기화
            user.setLoginStreaks(1);
        } else if (daysBetween == 1) {
            // 출석일 수 차이가 1일만 차이날 때 누적
            user.setLoginStreaks(user.getLoginStreaks() + 1);
        }
        userRepository.save(user);
    }

    public void addEXPAfterGamePlayed(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CommonException(ErrorCode.WRONG_USER));
        if (isLastDateEqualToToday(user.getLastGamePlayedDate())) {
            return;
        }
        addExpToUser(user, 1);
        user.setLastGamePlayedDate(LocalDate.now());
        userRepository.save(user);
    }

    // 오늘 일기를 안 썼을 때에만 호출되므로 검증 로직 불필요
    public void addEXPAfterDiaryWrote(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CommonException(ErrorCode.WRONG_USER));
        addExpToUser(user, 1);
        userRepository.save(user);
    }

    public void addEXPAfterQuizPlayed(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CommonException(ErrorCode.WRONG_USER));
        if (isLastDateEqualToToday(user.getLastQuizPlayedDate())) {
            return;
        }
        addExpToUser(user, 0.5F);
        user.setLastQuizPlayedDate(LocalDate.now());
        userRepository.save(user);
    }

    private boolean isLastDateEqualToToday(LocalDate lastPlayedDate) {
        if (lastPlayedDate == null) {
            return false;
        }
        return !lastPlayedDate.equals(LocalDate.now());
    }

    private void addExpToUser(User user, float exp) {
        float currentExp = user.getLevelEXP();
        user.setLevelEXP(currentExp + exp);
        updateLevel(user);
    }

    private void updateLevel(User user) {
        int level = calculateLevelFromExp(user.getLevelEXP(), user.getLevel());
        float remainExp = calculateRemainExp(user.getLevelEXP(), user.getLevel());
        user.setLevel(level);
        user.setLevelEXP(remainExp);
    }

    private float calculateRemainExp(float levelEXP, int level) {
        float remainExp = levelEXP - (float)Math.pow(3, level);
        if (remainExp >= 0.0F) {
            return remainExp;
        }
        return levelEXP;
    }

    private int calculateLevelFromExp(float levelEXP, int level) {
        return (float)Math.pow(3, level) <= levelEXP ? level + 1 : level;
    }

    public void addCoinAfterGamePlayed(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CommonException(ErrorCode.WRONG_USER));
        addCoinToUser(user, 50);
        userRepository.save(user);
    }

    public void addCoinAfterDiaryWrote(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CommonException(ErrorCode.WRONG_USER));
        addCoinToUser(user, 50);
        userRepository.save(user);
    }

    public void addCoinAfterQuizPlayed(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CommonException(ErrorCode.WRONG_USER));
        addCoinToUser(user, 20);
        userRepository.save(user);
    }

    private void addCoinToUser(User user, int coin) {
        int currentCoin = user.getCoin();
        user.setCoin(currentCoin + coin);
    }

    public HomeResponceDto returnLoginStreaks(Long id){
        // 요청 파라미터 검증
        if (id == null) {
            throw new CommonException(ErrorCode.MISSING_REQUEST_PARAMETER);
        }

        // 사용자 정보 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.WRONG_USER));

        return new HomeResponceDto(user.getLoginStreaks());
    }
}
