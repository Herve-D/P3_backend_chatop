package com.openclassrooms.chatop.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.chatop.entity.Rental;
import com.openclassrooms.chatop.service.FileUploadService;
import com.openclassrooms.chatop.service.RentalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class RentalController {

	private final Logger logger = LoggerFactory.getLogger(RentalController.class);

	@Autowired
	private RentalService rentalService;

	@Autowired
	private FileUploadService fileUpload;

	/**
	 * Create - Add a new rental
	 * 
	 * @param rental - An object rental
	 * @return The rental object saved
	 */
	@Operation(summary = "Create a rental", description = "Route for creating a new rental.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Rental created.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Rental.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
			@ApiResponse(responseCode = "404", description = "Rental not created.", content = @Content) })
	@PostMapping("/rentals")
	public ResponseEntity<Map<String, String>> createRental(
			@Parameter(description = "name") @Valid @RequestParam("name") String name,
			@Parameter(description = "surface") @Valid @RequestParam("surface") Double surface,
			@Parameter(description = "price") @Valid @RequestParam("price") Double price,
			@Parameter(description = "picture") @Valid @RequestParam("picture") MultipartFile picture,
			@Parameter(description = "description") @Valid @RequestParam("description") String description) {
		try {
			String pictureUrl = fileUpload.uploadFile(picture);
			rentalService.saveRental(name, surface, price, pictureUrl, description);
			return ResponseEntity.ok(Collections.singletonMap("message", "Rental created !"));

		} catch (Exception e) {
			// Handle any unexpected exceptions
			logger.error("Error occurred : " + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Update - Update an existing rental
	 * 
	 * @param id     - The id of the rental to update
	 * @param rental - The rental object updated
	 * @return
	 */
	@Operation(summary = "Update a rental", description = "Route for updating an existing rental.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Rental updated.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Rental.class))),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
			@ApiResponse(responseCode = "404", description = "Rental not found.", content = @Content) })
	@PutMapping("/rentals/{id}")
	public ResponseEntity<Map<String, String>> updateRental(
			@Parameter(description = "Rental to be updated") @Valid @PathVariable("id") Long id,
			@Parameter(description = "name") @Valid @RequestParam("name") String name,
			@Parameter(description = "surface") @Valid @RequestParam("surface") Double surface,
			@Parameter(description = "price") @Valid @RequestParam("price") Double price,
			@Parameter(description = "description") @Valid @RequestParam("description") String description) {
		try {
			rentalService.updateRental(id, name, surface, price, description);
			return ResponseEntity.ok(Collections.singletonMap("message", "Rental updated !"));
		} catch (Exception e) {
			// Handle any unexpected exceptions
			logger.error("Error occurred : " + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Read - Get one rental
	 * 
	 * @param id
	 * @return A rental object
	 */
	@Operation(summary = "Get a rental", description = "Route to get a rental from the Id.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Rental found.", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Rental.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid id", content = @Content),
			@ApiResponse(responseCode = "404", description = "Rental not found.", content = @Content) })
	@GetMapping("/rentals/{id}")
	public Rental getRental(
			@Parameter(description = "id of the rental to be found") @Valid @PathVariable("id") Long id) {
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
	 * @return A List object of all Rentals
	 */
	@Operation(summary = "Get all rentals", description = "Route to get a list of all the rentals.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Rentals found", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Rental.class))) }),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
			@ApiResponse(responseCode = "404", description = "Couldn't find any rentals.", content = @Content) })
	@GetMapping("/rentals")
	public ResponseEntity<Map<String, List<Rental>>> getRentals() {
		List<Rental> rentals = rentalService.getRentals();
		return ResponseEntity.ok(Collections.singletonMap("rentals", rentals));
	}

}
