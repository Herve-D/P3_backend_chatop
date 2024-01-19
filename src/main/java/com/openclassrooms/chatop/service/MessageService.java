package com.openclassrooms.chatop.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.chatop.entity.Message;
import com.openclassrooms.chatop.repository.IMessageRepository;

@Service
public class MessageService {

	@Autowired
	private IMessageRepository messageRepository;

	/**
	 * Posts a message for a rental.
	 * 
	 * @param message - The object message to save.
	 * @return The Message object.
	 */
	public Message saveMessage(Message message) {

		Message newMessage = new Message();
		newMessage.setRental_id(message.getRental_id());
		newMessage.setUser_id(message.getUser_id());
		newMessage.setMessage(message.getMessage());
		newMessage.setCreated_at(new Date());
		newMessage.setUpdated_at(new Date());

		Message savedMessage = messageRepository.save(newMessage);
		return savedMessage;
	}

}
