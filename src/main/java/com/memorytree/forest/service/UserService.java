package com.memorytree.forest.service;

import com.memorytree.forest.domain.User;
import com.memorytree.forest.exception.CommonException;
import com.memorytree.forest.exception.ErrorCode;
import com.memorytree.forest.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        addExpToUser(user, 1);
        userRepository.save(user);
    }

    public void addEXPAfterDiaryWrote(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CommonException(ErrorCode.WRONG_USER));
        addExpToUser(user, 1);
        userRepository.save(user);
    }

    public void addEXPAfterQuizPlayed(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CommonException(ErrorCode.WRONG_USER));
        addExpToUser(user, 0.5F);
        userRepository.save(user);
    }

    private void addExpToUser(User user, float exp) {
        user.setLevelEXP(exp);
        updateLevel(user);
    }

    private void updateLevel(User user) {
        int level = calculateLevelFromExp(user.getLevelEXP());
        user.setLevelEXP(level);
    }

    private int calculateLevelFromExp(float levelEXP) {
        int level = (int)Math.cbrt(levelEXP);
        if (level > 0) {
            return level;
        }
        return 1;
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
