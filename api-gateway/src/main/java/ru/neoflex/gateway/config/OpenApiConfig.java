package ru.neoflex.gateway.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "MS API-Gateway",
                description = "API Gateway действует как единая точка входа для всех клиентских запросов." +
                        " Он получает запросы от клиентов и маршрутизирует их к соответствующим микросервисам.",
                version = "1.0.0",
                contact = @Contact(
                        name = "Tatevosian Georgii"
                )
        )
)
public class OpenApiConfig {
}
