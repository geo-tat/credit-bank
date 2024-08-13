package ru.neoflex.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информация о графике платежей")
public class PaymentScheduleElementDto {
    @Schema(description = "Номер платежа")
    private Integer number;

    @Schema(description = "Дата платежа")
    private LocalDate date;

    @Schema(description = "Общая сумма платежа")
    private BigDecimal totalPayment;

    @Schema(description = "Сумма платежа по процентам")
    private BigDecimal interestPayment;

    @Schema(description = "Сумма платежа по основному долгу")
    private BigDecimal debtPayment;

    @Schema(description = "Оставшийся долг")
    private BigDecimal remainingDebt;
}
