package com.memorytree.forest.repository;

import com.memorytree.forest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
