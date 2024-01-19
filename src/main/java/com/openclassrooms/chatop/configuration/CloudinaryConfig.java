package com.openclassrooms.chatop.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {
	private final String CLOUD_NAME = "dvm8wn9q6";
	private final String API_KEY = "714783189991826";
	private final String API_SECRET = "VpbLYRFvHsE_xAgeJR6_wyMCSBc";

	@Bean
	public Cloudinary cloudinary() {
		Map<String, String> config = new HashMap<>();
		config.put("cloud_name", CLOUD_NAME);
		config.put("api_key", API_KEY);
		config.put("api_secret", API_SECRET);

		return new Cloudinary(config);
	}

}
