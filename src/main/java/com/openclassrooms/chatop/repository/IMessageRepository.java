package com.openclassrooms.chatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.chatop.entity.Message;

@Repository
public interface IMessageRepository extends JpaRepository<Message, Long> {

}
