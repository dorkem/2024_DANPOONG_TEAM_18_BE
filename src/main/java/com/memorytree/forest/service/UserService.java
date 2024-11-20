package com.memorytree.forest.service;

import com.memorytree.forest.domain.User;
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
}
