package ru.neoflex.calculator.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Schema(description = "Заявление на подбор кредита")
public class LoanStatementRequestDto {

    @Schema(description = "Имя", minLength = 2, maxLength = 30)
    private String firstName;

    @Schema(description = "Фамилия", minLength = 2, maxLength = 30)
    private String lastName;

    @Schema(description = "Отчество", minLength = 2, maxLength = 30)
    private String middleName;

    @Schema(description = "Сумма кредита, от 30000")
    private BigDecimal amount;

    @Schema(description = "Срок кредита, от 6 месяцев")
    private Integer term;

    @Schema(description = "Дата рождения", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate birthdate;

    @Schema(description = "Email адрес")
    private String email;

    @Schema(description = "Серия паспорта")
    private String passportSeries;

    @Schema(description = "Номер паспорта")
    private String passportNumber;
}
