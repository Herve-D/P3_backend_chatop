package com.openclassrooms.chatop.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.chatop.entity.Rental;
import com.openclassrooms.chatop.service.RentalService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class RentalController {

	private final Logger logger = LoggerFactory.getLogger(RentalController.class);

	@Autowired
	private RentalService rentalService;

	/**
	 * Create - Add a new rental
	 * 
	 * @param rental - An object rental
	 * @return The rental object saved
	 */
	@PostMapping("/rentals")
	public ResponseEntity<String> createRental(@RequestBody Rental rental) {
		logger.debug("ici");
		try {
			rentalService.saveRental(rental);
			return ResponseEntity.status(HttpStatus.CREATED).body("Rental created!");
		} catch (Exception e) {
			// Handle any unexpected exceptions
			logger.error("Error occurred : ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Read - Get one rental
	 * 
	 * @param id
	 * @return A rental object
	 */
	@GetMapping("/rentals/{id}")
	public Rental getRental(@PathVariable("id") Long id) {
		Optional<Rental> rental = rentalService.getRental(id);
		if (rental.isPresent()) {
			return rental.get();
		} else {
			return null;
		}
	}

	/**
	 * Read - Get all rentals
	 * 
	 * @return An Iterable object of all Rentals
	 */
	@GetMapping("/rentals")
	public Iterable<Rental> getRentals() {
		return rentalService.getRentals();
	}

	/**
	 * Update - Update an existing rental
	 * 
	 * @param id     - The id of the rental to update
	 * @param rental - The rental object updated
	 * @return
	 */
	@PutMapping("/rentals/{id}")
	public ResponseEntity<String> updateRental(@PathVariable("id") Long id, @RequestBody Rental rental) {
		rentalService.updateRental(rental);
		return ResponseEntity.ok("Rental updated!");
	}

	/**
	 * Delete - Delete a rental
	 * 
	 * @param id - The id of the rental to delete
	 */
	@DeleteMapping("/rentals/{id}")
	public void deleteRental(@PathVariable("id") Long id) {
		rentalService.deleteRental(id);
	}

}
