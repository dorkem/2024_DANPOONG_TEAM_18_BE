package com.memorytree.forest.service;

import com.memorytree.forest.domain.User;
import com.memorytree.forest.exception.CommonException;
import com.memorytree.forest.exception.ErrorCode;
import com.memorytree.forest.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(Long id, String name) {
        User user = User.builder()
                .id(id)
                .name(name)
                .build();
        return userRepository.save(user); // 새 User 저장
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
}
