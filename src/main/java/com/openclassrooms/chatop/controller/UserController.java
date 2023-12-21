package com.openclassrooms.chatop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatop.entity.ChatopUser;
import com.openclassrooms.chatop.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	/**
	 * Read - Get one user by ID.
	 * 
	 * @param id - The id of the user
	 * @return An user object filled
	 */
	@Operation(summary = "Get user by Id", description = "Route to retrieve an user by their Id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatopUser.class))),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
	@GetMapping("/user/{id}")
	public ResponseEntity<ChatopUser> getUserById(@PathVariable("id") Long id) {
		try {
			ChatopUser user = userService.getUserById(id);
			return ResponseEntity.ok(user);

		} catch (UsernameNotFoundException e) {
			// Handle the case where the user is not found
			logger.error("User not found : ", e.getMessage(), e);
			return ResponseEntity.notFound().build();

		} catch (Exception e) {
			// Handle any other unexpected exceptions
			logger.error("Error occurred while fetching user : ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
