package ru.neoflex.calculator.dto;

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
import ru.neoflex.calculator.annotation.Age;
import ru.neoflex.calculator.enums.Gender;
import ru.neoflex.calculator.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Заявление на полный расчет кредита")
public class ScoringDataDto {

    @Schema(description = "Сумма кредита, от 30000")
    @NotNull(message = "Loan amount cannot be null")
    @DecimalMin(value = "30000.00", message = "Loan amount must be greater than or equal to 30000")
    private BigDecimal amount;

    @NotNull(message = "Term cannot be null")
    @Min(value = 6, message = "Loan term must be greater than or equal to 6")
    @Schema(description = "Срок кредита, от 6 месяцев")
    private Integer term;

    @Schema(description = "Имя", minLength = 2, maxLength = 30)
    @NotBlank(message = "First name cannot be null or empty")
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only Latin letters")
    private String firstName;

    @Schema(description = "Фамилия", minLength = 2, maxLength = 30)
    @NotBlank(message = "Last name cannot be null or empty")
    @Size(min = 2, max = 30, message = "Last name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must contain only Latin letters")
    private String lastName;

    @Schema(description = "Отчество", minLength = 2, maxLength = 30)
    @Size(min = 2, max = 30, message = "Middle name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Middle name must contain only Latin letters")
    private String middleName;

    @Schema(description = "Пол", allowableValues = {"MALE", "FEMALE"})
    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    @Schema(description = "Дата рождения", format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "Birthdate cannot be null")
    @Past(message = "Birthdate must be in the past")
    @Age(min = 18, message = "Customer must be at least 18 years old")
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта")
    @NotBlank(message = "Passport series cannot be null or empty")
    @Pattern(regexp = "^\\d{4}$", message = "Passport series must be exactly 4 digits")
    private String passportSeries;

    @Schema(description = "Номер паспорта")
    @NotBlank(message = "Passport number cannot be null or empty")
    @Pattern(regexp = "^\\d{4}$", message = "Passport number must be exactly 6 digits")
    private String passportNumber;

    @Schema(description = "Дата выдачи паспорта")
    @NotNull(message = "Passport issue date cannot be null")
    @Past(message = "Passport issue date must be in the past")
    private LocalDate passportIssueDate;

    @NotBlank(message = "Passport issue branch cannot be null or empty")
    @Schema(description = "Кем выдан паспорт")
    private String passportIssueBranch;

    @Schema(description = "Семейное положение", allowableValues = {"SINGLE", "MARRIED", "WIDOWED", "DIVORCED"})
    @NotNull(message = "Marital status cannot be null")
    private MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев")
    @NotNull(message = "Dependent amount cannot be null")
    @Min(value = 0, message = "Dependent amount must be zero or greater")
    private Integer dependentAmount;

    @Schema(description = "Информация о месте работы")
    @NotNull(message = "Employment information cannot be null")
    private EmploymentDto employment;

    @Schema(description = "Номер счета")
    @NotNull(message = "Account number cannot be null")
    private String accountNumber;
    @Schema(description = "Наличие страховки")
    @NotNull(message = "Insurance status cannot be null")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Является зарплатным клиентом банка")
    @NotNull(message = "Salary client status cannot be null")
    private Boolean isSalaryClient;


}
