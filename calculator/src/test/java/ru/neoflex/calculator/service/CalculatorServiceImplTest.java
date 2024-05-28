package ru.neoflex.calculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.neoflex.calculator.DtoBuilder;
import ru.neoflex.calculator.dto.CreditDto;
import ru.neoflex.calculator.dto.LoanOfferDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.enums.EmploymentStatus;
import ru.neoflex.calculator.exception.LoanDeniedException;
import ru.neoflex.calculator.properties.LoanProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CalculatorServiceImplTest {
    @Autowired
    private CalculatorServiceImpl service;
    @Autowired
    private LoanProperties properties;

    LoanStatementRequestDto loanStatementRequestDto;
    ScoringDataDto scoringDataDto;

    @BeforeEach
    void setUp() {
        loanStatementRequestDto = DtoBuilder.getLoanStatementRequestDto();
        scoringDataDto = DtoBuilder.getScoringDataDto();
    }

    @Test
    void calculateLoanOffers() {
        // Given

        // When
        List<LoanOfferDto> list = service.calculateLoanOffers(loanStatementRequestDto);
        // Then
        assertEquals(4, list.size());
        assertEquals(loanStatementRequestDto.getLoanAmount(), list.getFirst().getRequestedAmount());
    }

    @Test
    void calculateCreditParameters() {
        // Given

        // When
        CreditDto creditDto = service.calculateCreditParameters(scoringDataDto);
        // Then
        assertEquals(scoringDataDto.getAmount(), creditDto.getAmount());
        assertEquals(creditDto.getMonthlyPayment(), creditDto.getPaymentSchedule().getFirst().getTotalPayment());
    }

    @Test
    void calculateCreditParametersWhenStatusUNEMPLOYED() {
        // Given
        scoringDataDto.getEmployment().setEmploymentStatus(EmploymentStatus.UNEMPLOYED);
        // When
        LoanDeniedException exception = assertThrows(LoanDeniedException.class, () -> {
            service.calculateCreditParameters(scoringDataDto);
        });
        // Then
        assertEquals("Loan denied. Customer is unemployed", exception.getMessage());
    }

    @Test
    void calculateCreditParametersWhenOlder60() {
        // Given
        scoringDataDto.setBirthdate(LocalDate.of(1954, 1, 1));
        // When
        LoanDeniedException exception = assertThrows(LoanDeniedException.class, () -> {
            service.calculateCreditParameters(scoringDataDto);
        });
        // Then
        assertEquals("Loan denied. Customer's age is invalid", exception.getMessage());
    }

    @Test
    void calculateCreditParametersWhenYounger21() {
        // Given
        scoringDataDto.setBirthdate(LocalDate.of(2014, 1, 1));
        // When
        LoanDeniedException exception = assertThrows(LoanDeniedException.class, () -> {
            service.calculateCreditParameters(scoringDataDto);
        });
        // Then
        assertEquals("Loan denied. Customer's age is invalid", exception.getMessage());
    }

    @Test
    void calculateCreditParametersWhenSalaryNotEnough() {
        // Given
        scoringDataDto.getEmployment().setSalary(BigDecimal.valueOf(10000));
        // When
        LoanDeniedException exception = assertThrows(LoanDeniedException.class, () -> {
            service.calculateCreditParameters(scoringDataDto);
        });
        // Then
        assertEquals("Loan denied. Customer's salary is not enough", exception.getMessage());
    }

    @Test
    void calculateCreditParametersWhenTotalWorkExperienceNotValid() {
        // Given
        scoringDataDto.getEmployment().setWorkExperienceTotal(20);
        // When
        LoanDeniedException exception = assertThrows(LoanDeniedException.class, () -> {
            service.calculateCreditParameters(scoringDataDto);
        });
        // Then
        assertEquals("Loan denied. Customer's total work experience less then 24 months", exception.getMessage());
    }

    @Test
    void calculateCreditParametersWhenCurrentWorkExperienceNotValid() {
        // Given
        scoringDataDto.getEmployment().setWorkExperienceCurrent(3);
        // When
        LoanDeniedException exception = assertThrows(LoanDeniedException.class, () -> {
            service.calculateCreditParameters(scoringDataDto);
        });
        // Then
        assertEquals("Loan denied. Customer's current work experience less then 6 months", exception.getMessage());
    }
}