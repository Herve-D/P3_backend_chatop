package com.openclassrooms.chatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.chatop.entity.Rental;

@Repository
public interface IRentalRepository extends JpaRepository<Rental, Long> {

}
