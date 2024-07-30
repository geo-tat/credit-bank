package ru.neoflex.dossier.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "gateway")
public class GatewayProperties {

    private String host;
}
