package ru.neoflex.deal.exception;

import lombok.Getter;

@Getter
public class OfferAlreadySelectedException extends RuntimeException {
    private final String message;

    public OfferAlreadySelectedException(String message) {
        this.message = message;
    }

}
