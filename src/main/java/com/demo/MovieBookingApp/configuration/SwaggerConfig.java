package com.demo.MovieBookingApp.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "My Movie Booking APP",
                version = "1.0",
                description = "APIs for managing a movie booking app",
                termsOfService = "http://example.com/terms",
                contact = @Contact(name = "Akshat Mishra", email = "akshat.mishra@gmail.com",
                        url = "http://example.com"),
                license = @License(name = "API License", url = "http://example.com/license")
        )
)
public class SwaggerConfig {
}
