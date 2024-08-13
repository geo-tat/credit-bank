package ru.neoflex.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информация о возможном условии кредита")
public class LoanOfferDto {
    @Schema(description = "Идентификатор заявки")
    private UUID statementId;

    @Schema(description = "Запрашиваемая сумма кредита")
    private BigDecimal requestedAmount;

    @Schema(description = "Общая сумма кредита")
    private BigDecimal totalAmount;

    @Schema(description = "Срок кредита в месяцах")
    private Integer term;

    @Schema(description = "Ежемесячный платеж")
    private BigDecimal monthlyPayment;

    @Schema(description = "Процентная ставка")
    private BigDecimal rate;

    @Schema(description = "Наличие страховки")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Является зарплатным клиентом")
    private Boolean isSalaryClient;
}
