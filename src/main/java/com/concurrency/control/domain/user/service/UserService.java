package com.concurrency.control.domain.user.service;

import com.concurrency.control.domain.user.entity.User;
import com.concurrency.control.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public User getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new RuntimeException());
	}

	@Transactional
	public User save(User entity) {
		return userRepository.save(entity);
	}

}
