package ru.neoflex.deal.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS Deal",
                version = "1.0.0",
                contact = @Contact(
                        name = "Tatevosian Georgii"
                )
        )
)
public class OpenApiConfig {

}
