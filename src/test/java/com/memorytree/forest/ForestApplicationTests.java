package com.memorytree.forest;

import com.memorytree.forest.domain.User;
import com.memorytree.forest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ForestApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private UserRepository userRepository;

	@Test
	public void testDatabaseConnection() {
		Optional<User> user = userRepository.findById(2L);
		assertTrue(user.isPresent());
		System.out.println("User exists: " + user.get().getName());
	}

}
