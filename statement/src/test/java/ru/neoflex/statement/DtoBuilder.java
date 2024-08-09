package ru.neoflex.statement;


import ru.neoflex.statement.dto.LoanOfferDto;
import ru.neoflex.statement.dto.LoanStatementRequestDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class DtoBuilder {

    public static LoanStatementRequestDto getLoanStatementRequestDto() {
        return LoanStatementRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .middleName("Michael")
                .amount(BigDecimal.valueOf(400000))
                .term(24)
                .birthdate(LocalDate.of(1990, 1, 1))
                .email("john.doe@example.com")
                .passportSeries("1234")
                .passportNumber("567890")
                .build();
    }


    public static LoanOfferDto getLoanOfferDto() {
        return LoanOfferDto.builder()
                .statementId(UUID.randomUUID())
                .requestedAmount(BigDecimal.valueOf(403000))
                .term(14)
                .rate(BigDecimal.valueOf(9.0))
                .monthlyPayment(BigDecimal.valueOf(30862.91))
                .totalAmount(BigDecimal.valueOf(432080.74))
                .isSalaryClient(true)
                .isInsuranceEnabled(true)
                .build();
    }


}
