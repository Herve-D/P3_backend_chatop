package com.openclassrooms.chatop.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.chatop.entity.ChatopUser;
import com.openclassrooms.chatop.repository.IUserRepository;

@Service
public class UserService {

	@Autowired
	private IUserRepository userRepository;

	public ChatopUser getUserById(Long id) {
		Optional<ChatopUser> userOptional = userRepository.findById(id);
		if (userOptional.isPresent()) {
			ChatopUser user = userOptional.get();
			return user;
		}
		return null;
	}

	public Iterable<ChatopUser> getUsers() {
		return userRepository.findAll();
	}

	public ChatopUser saveUser(ChatopUser user) {
		ChatopUser savedUser = userRepository.save(user);
		return savedUser;
	}

	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

}
