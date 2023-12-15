package com.openclassrooms.chatop.service;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.chatop.configuration.JwtService;
import com.openclassrooms.chatop.entity.ChatopUser;
import com.openclassrooms.chatop.entity.Login;
import com.openclassrooms.chatop.repository.IUserRepository;

@Service
public class AuthService {

	private final Logger logger = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	/**
	 * Check if email's user already exists.
	 * 
	 * @param email
	 * @return true if user with given email exists, false otherwise.
	 */
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	/**
	 * Register a new user.
	 * 
	 * @param user - The ChatopUser object representing the new use to register.
	 * @return The UserDetails object representing the registered user.
	 * @throws IllegalArgumentException if a user with the same email already
	 *                                  exists.
	 */
	public UserDetails register(ChatopUser user) {
		if (existsByEmail(user.getEmail())) {
			throw new IllegalArgumentException("Email already exists.");
		}
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		
		user.setPassword(encodedPassword);
		user.setName(user.getName());
		user.setEmail(user.getEmail());
		user.setCreated_at(new Date());
		user.setUpdated_at(new Date());
		
		userRepository.save(user);

		UserDetails userDetails = new User(user.getEmail(), user.getPassword(), Collections.emptyList());
		
		return userDetails;
	}

	public String login(Login login) throws BadCredentialsException {
		try {
			Authentication authenticate = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(login.getLogin(), login.getPassword()));

			User authenticatedUser = (User) authenticate.getPrincipal();
			String token = jwtService.generateToken(authenticatedUser);
			logger.info("Token is : " + token);

			return token;
		} catch (AuthenticationException ex) {
			throw new BadCredentialsException("Invalid credentials");
		}
	}

	public ChatopUser getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}


		String name = authentication.getName();

		Optional<ChatopUser> userOptional = userRepository.findByEmail(name);
		if (userOptional.isPresent()) {
			ChatopUser user = userOptional.get();
			return user;
		}
		return null;
	}

}
