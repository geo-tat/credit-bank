package ru.neoflex.calculator.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AgeValidator implements ConstraintValidator<Age, LocalDate> {
    private int minAge;

    @Override
    public void initialize(Age age) {
        this.minAge = age.min();
    }

    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        if (birthdate == null) {
            return true;
        }
        return Period.between(birthdate, LocalDate.now()).getYears() >= minAge;
    }
}
