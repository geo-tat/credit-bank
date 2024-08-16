package ru.neoflex.statement.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS Statement",
                version = "1.0.0",
                description = "Осуществляет прескоринг заявки",
                contact = @Contact(
                        name = "Tatevosian Georgii"
                )
        )
)
public class OpenApiConfig {
}
