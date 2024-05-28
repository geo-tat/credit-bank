package ru.neoflex.calculator;

import ru.neoflex.calculator.dto.EmploymentDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.enums.EmploymentStatus;
import ru.neoflex.calculator.enums.Gender;
import ru.neoflex.calculator.enums.MaritalStatus;
import ru.neoflex.calculator.enums.Position;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DtoBuilder {

    private LoanStatementRequestDto loanStatementRequestDto;
    private ScoringDataDto scoringDataDto;

    public static LoanStatementRequestDto getLoanStatementRequestDto() {
        return LoanStatementRequestDto.builder()
                .firstName("John")
                .lastName("Doe")
                .middleName("Michael")
                .loanAmount(BigDecimal.valueOf(400000))
                .loanTerm(24)
                .birthdate(LocalDate.of(1990, 1, 1))
                .email("john.doe@example.com")
                .passportSeries("1234")
                .passportNumber("567890")
                .build();
    }

    public static ScoringDataDto getScoringDataDto() {
        return ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(400000))
                .term(12)
                .firstName("Ivan")
                .lastName("Petrov")
                .middleName("Ally")
                .gender(Gender.MALE)
                .birthdate(LocalDate.of(1990, 1, 1))
                .passportSeries("1234")
                .passportNumber("567890")
                .passportIssueDate(LocalDate.of(2010, 1, 1))
                .passportIssueBranch("Отделениe УФМС")
                .maritalStatus(MaritalStatus.SINGLE)
                .dependentAmount(2)
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .employerINN("1234567890")
                        .salary(BigDecimal.valueOf(70000))
                        .position(Position.TOP_MANAGER)
                        .workExperienceTotal(120)
                        .workExperienceCurrent(60)
                        .build())
                .accountNumber("12345678901234567890")
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .build();
    }
}
