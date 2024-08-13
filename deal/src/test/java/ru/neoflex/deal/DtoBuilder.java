package ru.neoflex.deal;

import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.dto.EmploymentDto;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.dto.PassportDto;
import ru.neoflex.deal.dto.PaymentScheduleElementDto;
import ru.neoflex.deal.dto.ScoringDataDto;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.enums.EmploymentStatus;
import ru.neoflex.deal.enums.Gender;
import ru.neoflex.deal.enums.MaritalStatus;
import ru.neoflex.deal.enums.Position;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    public static FinishRegistrationRequestDto getFinishDto() {
        return FinishRegistrationRequestDto.builder()
                .accountNumber("12345678901234567890")
                .employmentDto(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .employerINN("1234567890")
                        .salary(BigDecimal.valueOf(70000))
                        .position(Position.TOP_MANAGER)
                        .workExperienceTotal(120)
                        .workExperienceCurrent(60)
                        .build())
                .dependentAmount(2)
                .maritalStatus(MaritalStatus.SINGLE)
                .passportIssueBranch("Отделениe УФМС")
                .passportIssueDate(LocalDate.of(2010, 1, 1))
                .gender(Gender.MALE)
                .build();
    }

    public static Client getClient(FinishRegistrationRequestDto finishRegistrationRequestDto,
                                   LoanStatementRequestDto requestDto) {
        return Client.builder()
                .id(UUID.randomUUID())
                .accountNumber(finishRegistrationRequestDto.getAccountNumber())
                .dependentAmount(finishRegistrationRequestDto.getDependentAmount())
                .email(requestDto.getEmail())
                .employmentData(finishRegistrationRequestDto.getEmploymentDto())
                .maritalStatus(finishRegistrationRequestDto.getMaritalStatus())
                .middleName(requestDto.getMiddleName())
                .birthDate(requestDto.getBirthdate())
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .gender(finishRegistrationRequestDto.getGender())
                .passportDtoData(PassportDto.builder()
                        .issueBranch(finishRegistrationRequestDto.getPassportIssueBranch())
                        .issueDate(finishRegistrationRequestDto.getPassportIssueDate())
                        .number(requestDto.getPassportNumber())
                        .series(requestDto.getPassportSeries())
                        .build())
                .build();
    }

    public static CreditDto getCreditDto() {
        List<PaymentScheduleElementDto> list = new ArrayList<>();
        return CreditDto.builder()
                .amount(BigDecimal.valueOf(403000))
                .psk(BigDecimal.valueOf(432080.74))
                .paymentSchedule(list)
                .isSalaryClient(true)
                .isInsuranceEnabled(true)
                .monthlyPayment(BigDecimal.valueOf(30862.91))
                .rate(BigDecimal.valueOf(9.0))
                .term(14)
                .build();
    }
}
