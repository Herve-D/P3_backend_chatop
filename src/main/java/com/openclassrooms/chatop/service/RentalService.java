package com.openclassrooms.chatop.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.chatop.entity.ChatopUser;
import com.openclassrooms.chatop.entity.Rental;
import com.openclassrooms.chatop.repository.IRentalRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RentalService {

	@Autowired
	private IRentalRepository rentalRepository;
	
	@Autowired
	private AuthService authService;

	public Optional<Rental> getRental(Long id) {
		return rentalRepository.findById(id);
	}

	public Iterable<Rental> getRentals() {
		return rentalRepository.findAll();
	}

	public Rental updateRental(Rental rental) {
		Rental updatedRental = rentalRepository.findById(rental.getId())
				.orElseThrow(() -> new EntityNotFoundException("Rental not found."));
		updatedRental.setName(rental.getName());
		updatedRental.setSurface(rental.getSurface());
		updatedRental.setPrice(rental.getPrice());
		updatedRental.setDescription(rental.getDescription());
		updatedRental.setCreated_at(updatedRental.getCreated_at());
		updatedRental.setUpdated_at(new Date());
		return rentalRepository.save(updatedRental);
	}

	public Rental saveRental(Rental rental) {
		ChatopUser user=authService.getCurrentUser();
		//TODO
		System.out.print( rental.getName());
		rental.setSurface(rental.getSurface());
		rental.setPrice(rental.getPrice());
		rental.setPicture(rental.getPicture());
		rental.setDescription(rental.getDescription());
		rental.setOwner_id(user.getUserId());
		rental.setCreated_at(new Date());
		rental.setUpdated_at(new Date());
		Rental savedRental = rentalRepository.save(rental);
		return savedRental;
	}

	public void deleteRental(Long id) {
		rentalRepository.deleteById(id);
	}

}
