package com.openclassrooms.chatop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.chatop.entity.ChatopUser;

@Repository
public interface IUserRepository extends JpaRepository<ChatopUser, Integer> {

	List<ChatopUser> findByName(String name);

}
