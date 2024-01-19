package com.openclassrooms.chatop.controller;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatop.entity.Message;
import com.openclassrooms.chatop.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class MessageController {

	private final Logger logger = LoggerFactory.getLogger(MessageController.class);

	@Autowired
	private MessageService messageService;

	/**
	 * Create - Add a new message
	 * 
	 * @param message - An object message
	 * @return The message object saved
	 */
	@Operation(summary = "Send a message", description = "Route for sending a new message.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Message sent.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
			@ApiResponse(responseCode = "404", description = "Message not sent.", content = @Content) })
	@PostMapping("/messages")
	public ResponseEntity<Map<String, String>> createMessage(@RequestBody Message message) {

		try {
			logger.info("Message is : " + message.getMessage());
			messageService.saveMessage(message);
			return ResponseEntity.ok(Collections.singletonMap("message", "Message sent !"));

		} catch (IllegalArgumentException e) {
			// Handle invalid input or other business logic errors
			logger.error("Exception is : " + e);
			return ResponseEntity.badRequest().build();

		} catch (DataAccessException e) {
			// Handle database-related exceptions
			logger.error("Exception is : " + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		} catch (Exception e) {
			// Handle any other unexpected exceptions
			logger.error("Exception is : " + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
