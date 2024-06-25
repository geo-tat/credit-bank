package ru.neoflex.statement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableFeignClients
public class StatementApp {

    public static void main(String[] args) {
        SpringApplication.run(StatementApp.class, args);
    }

}
