package ru.neoflex.deal.mapper;

import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.dto.PassportDto;
import ru.neoflex.deal.dto.ScoringDataDto;
import ru.neoflex.deal.dto.StatementStatusHistoryDto;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.entity.Credit;
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.deal.enums.ApplicationStatus;
import ru.neoflex.deal.enums.ChangeType;
import ru.neoflex.deal.enums.CreditStatus;

import java.time.LocalDateTime;
import java.util.List;

public class DealMapper {

    private DealMapper() {
    }

    public static Client initializeClient(LoanStatementRequestDto dto) {
        return Client.builder()
                .email(dto.getEmail())
                .birthDate(dto.getBirthdate())
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .lastName(dto.getLastName())
                .passportDtoData(PassportDto.builder()
                        .series(dto.getPassportSeries())
                        .number(dto.getPassportNumber())
                        .build())
                .build();
    }

    public static Statement initializeStatement(LocalDateTime registrationTime,
                                                Client client) {
        return Statement.builder()
                .client(client)
                .creationDate(registrationTime)
                .statusHistory(List.of(StatementStatusHistoryDto.builder()
                        .status(ApplicationStatus.PREAPPROVAL)
                        .changeType(ChangeType.AUTOMATIC)
                        .time(registrationTime)
                        .build()))
                .applicationStatus(ApplicationStatus.PREAPPROVAL)
                .build();
    }

    public static ScoringDataDto initializeScoringDataDto(FinishRegistrationRequestDto dto, Statement statement) {
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .firstName(statement.getClient().getFirstName())
                .lastName(statement.getClient().getLastName())
                .accountNumber(dto.getAccountNumber())
                .birthdate(statement.getClient().getBirthDate())
                .dependentAmount(dto.getDependentAmount())
                .employment(dto.getEmploymentDto())
                .gender(dto.getGender())
                .isSalaryClient(statement.getAppliedOffer().getIsSalaryClient())
                .isInsuranceEnabled(statement.getAppliedOffer().getIsInsuranceEnabled())
                .maritalStatus(dto.getMaritalStatus())
                .amount(statement.getAppliedOffer().getTotalAmount())
                .passportNumber(statement.getClient().getPassportDtoData().getNumber())
                .passportSeries(statement.getClient().getPassportDtoData().getSeries())
                .passportIssueBranch(dto.getPassportIssueBranch())
                .passportIssueDate(dto.getPassportIssueDate())
                .term(statement.getAppliedOffer().getTerm())
                .build();
        if (statement.getClient().getMiddleName() != null) {
            scoringDataDto.setMiddleName(statement.getClient().getMiddleName());
        }

        return scoringDataDto;
    }

    public static Credit initializeCredit(CreditDto creditDto) {
        return Credit.builder()
                .creditStatus(CreditStatus.CALCULATED)
                .psk(creditDto.getPsk())
                .amount(creditDto.getAmount())
                .term(creditDto.getTerm())
                .rate(creditDto.getRate())
                .insuranceEnabled(creditDto.getIsInsuranceEnabled())
                .monthlyPayment(creditDto.getMonthlyPayment())
                .paymentSchedule(creditDto.getPaymentSchedule())
                .salaryClient(creditDto.getIsSalaryClient())
                .build();
    }
}
