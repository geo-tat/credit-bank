package ru.neoflex.gateway.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "MS API-Gateway",
                version = "1.0.0",
                contact = @Contact(
                        name = "Tatevosian Georgii"
                )
        )
)
public class OpenApiConfig {
}
