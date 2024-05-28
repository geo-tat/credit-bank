package ru.neoflex.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информация о расчете кредита")
public class CreditDto {
    @Schema(description = "Customize Toolbar...")
    private BigDecimal amount;
    @Schema(description = "Срок кредита в месяцах")
    private Integer term;
    @Schema(description = "Ежемесячный платеж")
    private BigDecimal monthlyPayment;
    @Schema(description = "Кредитная ставка")
    private BigDecimal rate;
    @Schema(description = "Полная стоимость кредита")
    private BigDecimal psk;
    @Schema(description = "Наличие страховки")
    private Boolean isInsuranceEnabled;
    @Schema(description = "Является зарплатным клиентом")
    private Boolean isSalaryClient;
    @Schema(description = "График платежей")
    private List<PaymentScheduleElementDto> paymentSchedule;
}
