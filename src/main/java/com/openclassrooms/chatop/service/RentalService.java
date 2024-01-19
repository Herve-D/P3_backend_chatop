package com.openclassrooms.chatop.service;

import java.util.Date;
import java.util.List;
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

	public List<Rental> getRentals() {
		return rentalRepository.findAll();
	}

	public Rental saveRental(String name, Double surface, Double price, String picture, String description) {
		ChatopUser user = authService.getCurrentUser();

		Rental rental = new Rental();
		rental.setName(name);
		rental.setSurface(surface);
		rental.setPrice(price);
		rental.setPicture(picture);
		rental.setDescription(description);
		rental.setOwner_id(user.getId());
		rental.setCreated_at(new Date());
		rental.setUpdated_at(new Date());

		Rental savedRental = rentalRepository.save(rental);
		return savedRental;
	}

	public Rental updateRental(Long id, String name, Double surface, Double price, String description) {
		Rental updatedRental = rentalRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Rental not found."));
		updatedRental.setName(name);
		updatedRental.setSurface(surface);
		updatedRental.setPrice(price);
		updatedRental.setDescription(description);
		updatedRental.setUpdated_at(new Date());
		return rentalRepository.save(updatedRental);
	}

//	public Rental updateRental(Rental rental) {
//		Rental updatedRental = rentalRepository.findById(rental.getId())
//				.orElseThrow(() -> new EntityNotFoundException("Rental not found."));
//		updatedRental.setName(rental.getName());
//		updatedRental.setSurface(rental.getSurface());
//		updatedRental.setPrice(rental.getPrice());
//		updatedRental.setDescription(rental.getDescription());
//		updatedRental.setCreated_at(updatedRental.getCreated_at());
//		updatedRental.setUpdated_at(new Date());
//		return rentalRepository.save(updatedRental);
//	}

	public void deleteRental(Long id) {
		rentalRepository.deleteById(id);
	}

}
