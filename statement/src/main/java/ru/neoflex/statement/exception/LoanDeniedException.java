package ru.neoflex.statement.exception;

import lombok.Getter;

@Getter
public class LoanDeniedException extends RuntimeException {
    private final String message;

    public LoanDeniedException(String message) {
        this.message = message;
    }
}
