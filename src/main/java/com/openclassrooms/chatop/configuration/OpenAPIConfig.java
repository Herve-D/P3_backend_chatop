package com.openclassrooms.chatop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenAPIConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Chatop API")
						.description("API documentation for Chatop rental application")
						.version("v0.0.1")
						.license(new License().name("Apache 2.0").url("http://springdoc.org")))
				.components(new Components()
						.addSecuritySchemes("Bearer Authentication", new SecurityScheme().
								name("Bearer Authentication")
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")))
				.addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
	}

}
