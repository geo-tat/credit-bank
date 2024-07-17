package ru.neoflex.deal.exception;

import lombok.Getter;

@Getter
public class SesCodeIsNotValidException extends RuntimeException {
    private final String message;

    public SesCodeIsNotValidException(String message) {
        this.message = message;
    }
}
