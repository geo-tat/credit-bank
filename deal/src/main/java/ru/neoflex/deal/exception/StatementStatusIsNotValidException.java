package ru.neoflex.deal.exception;

import lombok.Getter;

@Getter
public class StatementStatusIsNotValidException extends RuntimeException {
    private final String message;

    public StatementStatusIsNotValidException(String message) {
        this.message = message;
    }
}
