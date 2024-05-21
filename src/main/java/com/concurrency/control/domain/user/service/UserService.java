package com.concurrency.control.domain.user.service;

import com.concurrency.control.domain.trade.entity.Trade;
import com.concurrency.control.domain.user.entity.User;
import com.concurrency.control.domain.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

	@Getter
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public User getUserById(Long userId) {
		var userOpt = userRepository.findById(userId);
		return userOpt.orElseThrow(() -> new RuntimeException());
	}

	@Transactional(readOnly = true)
	public List<User> getUserList() {
		return userRepository.findAll();
	}

	@Transactional
	public User save(User entity) {
		return userRepository.save(entity);
	}

	@Transactional
	public void tradeConfirmRequest(User user, Integer price) {
		user.deceasePoint(price);
		this.save(user);
	}

}
