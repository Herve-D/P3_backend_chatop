package com.openclassrooms.chatop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.chatop.entity.ChatopUser;

@Repository
public interface IUserRepository extends JpaRepository<ChatopUser, Long> {

	Optional<ChatopUser> findByEmail(String email);

	boolean existsByEmail(String email);

}
