package ru.neoflex.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.calculator.enums.EmploymentStatus;
import ru.neoflex.calculator.enums.Position;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информация о занятости заявителя на кредит")
public class EmploymentDto {

    @Schema(description = "Статус занятости заявителя",
            allowableValues = {"EMPLOYED", "SELF_EMPLOYED", "UNEMPLOYED"})
    @NotNull(message = "Employment status cannot be null")
    private EmploymentStatus employmentStatus;

    @Schema(description = "ИНН работодателя", example = "7707083893")
    @NotBlank(message = "Employer INN cannot be null")
    private String employerINN;

    @NotNull(message = "Salary cannot be null")
    @PositiveOrZero(message = "Salary must be positive")
    @Schema(description = "Ежемесячная зарплата заявителя", example = "5000")
    private BigDecimal salary;

    @NotNull(message = "Position cannot be null")
    @Schema(description = "Должность заявителя", allowableValues = {"""
            TOP_MANAGER,
            MIDDLE_MANAGER,
            WORKER,
            ENGINEER,
            DIRECTOR,
            OTHER"""})
    private Position position;

    @NotNull(message = "Total work experience cannot be null")
    @Min(value = 0, message = "Total work experience must be zero or greater")
    @Schema(description = "Общий опыт работы в месяцах", minimum = "0")
    private Integer workExperienceTotal;

    @NotNull(message = "Current work experience cannot be null")
    @Min(value = 0, message = "Current work experience must be zero or greater")
    @Schema(description = "Текущий опыт работы в месяцах", minimum = "0")
    private Integer workExperienceCurrent;
}
