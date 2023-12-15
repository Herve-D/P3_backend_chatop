package com.openclassrooms.chatop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatop.configuration.JwtService;
import com.openclassrooms.chatop.entity.ChatopUser;
import com.openclassrooms.chatop.entity.Login;
import com.openclassrooms.chatop.service.AuthService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth")
public class AuthController {

	private final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthService authService;

	/**
	 * Register a new user.
	 * 
	 * @param user - The user object to be registered.
	 * @return A response entity containing the registered user and access token.
	 */
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody ChatopUser user) {
		try {
			// Create a new user
			User registeredUser = (User) authService.register(user);

			// Generate the access token
			String token = jwtService.generateToken(registeredUser);
			logger.info("Token is : " + token);

			return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token)
					.body(registeredUser.getUsername() + " successfully authenticated.");

		} catch (Exception e) {
			// Handle any unexpected exceptions
			logger.error("Error occurred while registering user : ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * User login.
	 * 
	 * @param login - The login object containing user credentials.
	 * @return A response entity containing the authentication response.
	 */
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Login login) {
		
		logger.error("login controller : " + login.getLogin() + " " + login.getPassword());
		
		try {
			String authSuccess = authService.login(login);
			return ResponseEntity.ok().body(authSuccess);

		} catch (Exception e) {
			// Handle any unexpected exceptions
			logger.error("Error occurred while fetching current user : ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Get current user.
	 * 
	 * @return A response entity containing the current logged in user.
	 */
	@GetMapping("/me")
	public ResponseEntity<ChatopUser> getCurrentUser() {
		try {
			ChatopUser user = authService.getCurrentUser();
			if (user != null) {
				return ResponseEntity.ok(user);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (UsernameNotFoundException e) {
			// Handle the case where the user is not found
			logger.error("User not found : ", e.getMessage(), e);
			return ResponseEntity.notFound().build();

		} catch (Exception e) {
			// Handle any other unexpected exceptions
			logger.error("Error occurred while fetching current user : ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
