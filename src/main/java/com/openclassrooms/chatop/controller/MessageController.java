package com.openclassrooms.chatop.controller;

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
	@PostMapping("/messages")
	public ResponseEntity<String> createMessage(@RequestBody Message message) {
		try {
			logger.info("Message is : ", message.getRental_id());

			messageService.saveMessage(message);
			return ResponseEntity.ok("Message sent!");

		} catch (IllegalArgumentException e) {
			// Handle invalid input or other business logic errors
			logger.error("Exception is : ", e);
			return ResponseEntity.badRequest().build();

		} catch (DataAccessException e) {
			// Handle database-related exceptions
			logger.error("Exception is : ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		} catch (Exception e) {
			// Handle any other unexpected exceptions
			logger.error("Exception is : ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
