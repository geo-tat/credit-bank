package ru.neoflex.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class GatewayApp {
    public static void main( String[] args )
    {
        SpringApplication.run(GatewayApp.class, args);
    }
}
