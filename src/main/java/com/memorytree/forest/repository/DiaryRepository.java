package com.memorytree.forest.repository;

import com.memorytree.forest.domain.Diary;
import com.memorytree.forest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Optional<Diary> findByWhenAndUser(LocalDate when, User user);
}
