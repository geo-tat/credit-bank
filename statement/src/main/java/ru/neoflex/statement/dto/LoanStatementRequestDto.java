package ru.neoflex.statement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.statement.annotation.Age;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Заявление на подбор кредита")
public class LoanStatementRequestDto {

    @Schema(description = "Имя", minLength = 2, maxLength = 30)
    @NotBlank(message = "First name cannot be null or empty")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only Latin letters")
    private String firstName;

    @Schema(description = "Фамилия", minLength = 2, maxLength = 30)
    @NotBlank(message = "Last name cannot be null or empty")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must contain only Latin letters")
    private String lastName;

    @Schema(description = "Отчество", minLength = 2, maxLength = 30)
    @Size(min = 2, max = 30, message = "Middle name must be between 0 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Middle name must contain only Latin letters")
    private String middleName;

    @Schema(description = "Сумма кредита, от 30000")
    @NotNull(message = "Loan amount cannot be null")
    @DecimalMin(value = "30000.00", message = "Loan amount must be greater than or equal to 30000")
    private BigDecimal amount;

    @Schema(description = "Срок кредита, от 6 месяцев")
    @NotNull(message = "Loan term cannot be null")
    @Min(value = 6, message = "Loan term must be greater than or equal to 6")
    private Integer term;

    @Schema(description = "Дата рождения", format = "yyyy-MM-dd")
    @NotNull(message = "Birthdate cannot be null")
    @Past(message = "Birthdate must be in the past")
    @Age(min = 18, message = "Must be more or 18 years old")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate birthdate;

    @Schema(description = "Email адрес")
    @NotBlank(message = "Email cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email format")
    private String email;

    @Schema(description = "Серия паспорта")
    @NotBlank(message = "Passport series cannot be null or empty")
    @Pattern(regexp = "^\\d{4}$", message = "Passport series must be exactly 4 digits")
    private String passportSeries;

    @Schema(description = "Номер паспорта")
    @NotBlank(message = "Passport number cannot be null or empty")
    @Pattern(regexp = "^\\d{6}$", message = "Passport number must be exactly 6 digits")
    private String passportNumber;
}
