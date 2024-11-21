package com.memorytree.forest.repository;

import com.memorytree.forest.domain.Game;
import com.memorytree.forest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByUser(User user);
}
