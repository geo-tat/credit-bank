package ru.neoflex.deal.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MS Deal",
                description = "Сортирует поступающую информацию, сохраняет в базу данных." +
                        "Здесь осуществляется логика смены статуса заявки и кредита",
                version = "1.0.0",
                contact = @Contact(
                        name = "Tatevosian Georgii"
                )
        )
)
public class OpenApiConfig {

}
