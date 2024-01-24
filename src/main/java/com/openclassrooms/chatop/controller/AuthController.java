package com.openclassrooms.chatop.controller;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

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
	@Operation(summary = "Register a new user", description = "Route for registering a new user.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User registered.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatopUser.class))),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
			@ApiResponse(responseCode = "404", description = "Internal server error.", content = @Content) })
	@PostMapping("/register")
	public ResponseEntity<Map<String, String>> register(@Valid @RequestBody ChatopUser user) {
		try {
			// Create a new user
			User registeredUser = (User) authService.register(user);

			// Generate the access token
			String token = jwtService.generateToken(registeredUser);
			logger.info("Token is : " + token);

			return ResponseEntity.ok(Collections.singletonMap("token", token));

		} catch (Exception e) {
			// Handle any unexpected exceptions
			logger.error("Error occurred while registering user : " + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * User login.
	 * 
	 * @param login - The login object containing user credentials.
	 * @return A response entity containing the authentication response.
	 */
	@Operation(summary = "User login", description = "Route for user login.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Login successful.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Login.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@Valid @RequestBody Login login) {

		try {
			String token = authService.login(login);
			return ResponseEntity.ok(Collections.singletonMap("token", token));

		} catch (Exception e) {
			// Handle any unexpected exceptions
			logger.error("Error occurred while fetching current user : " + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Get current user.
	 * 
	 * @return A response entity containing the current logged in user.
	 */
	@Operation(summary = "Get current user", description = "Route to get the currently logged-in user.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Current user found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatopUser.class))),
			@ApiResponse(responseCode = "404", description = "Bad request", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
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
			logger.error("User not found : " + e.getMessage(), e);
			return ResponseEntity.notFound().build();

		} catch (Exception e) {
			// Handle any other unexpected exceptions
			logger.error("Error occurred while fetching current user : " + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
