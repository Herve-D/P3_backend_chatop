package com.openclassrooms.chatop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.chatop.entity.ChatopUser;
import com.openclassrooms.chatop.entity.Message;
import com.openclassrooms.chatop.repository.IMessageRepository;

@Service
public class MessageService {

	@Autowired
	private IMessageRepository messageRepository;
	
	@Autowired
	private AuthService authService;

	/**
	 * Posts a message for a rental.
	 * 
	 * @param message - The object message to save.
	 * @return The Message object.
	 */
	public Message saveMessage(Message message) {
		ChatopUser user=authService.getCurrentUser();
		Message savedMessage = messageRepository.save(message);
		return savedMessage;
	}

}
