package ru.neoflex.calculator.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
/**
 *  doc http://localhost:8080/swagger-ui
 *  OAS: http://localhost:8080/v3/api-docs
 */

@OpenAPIDefinition(
        info = @Info(
                title = "MS Calculator",
                version = "1.0.0",
                contact = @Contact(
                        name = "Tatevosian Georgii"
                )
        )
)
public class OpenApiConfig {

}
