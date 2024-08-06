package ru.neoflex.calculator.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        log.error("Ошибка валидации сущности", e);
        return new ErrorResponse(e.getClass().getSimpleName(),
                Arrays.stream(e.getStackTrace()).findFirst().toString(),
                e.getBindingResult().getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", ")),
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleCreditDeniedException(LoanDeniedException e) {
        log.info("Скоринг не пройден. В кредите отказано.");
        return new ErrorResponse(e.getClass().getSimpleName(),
                Arrays.stream(e.getStackTrace()).findFirst().toString(),
                e.getMessage(),
                LocalDateTime.now());
    }
}
