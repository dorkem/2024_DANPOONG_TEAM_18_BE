package com.memorytree.forest.service;

import com.memorytree.forest.domain.User;
import com.memorytree.forest.dto.global.ResponseDto;
import com.memorytree.forest.exception.CommonException;
import com.memorytree.forest.exception.ErrorCode;
import com.memorytree.forest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

}
