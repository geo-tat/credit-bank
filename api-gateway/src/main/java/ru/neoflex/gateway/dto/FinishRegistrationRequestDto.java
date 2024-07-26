package ru.neoflex.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.gateway.enums.Gender;
import ru.neoflex.gateway.enums.MaritalStatus;


import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Заявление на окончательное оформление кредита")
public class FinishRegistrationRequestDto {

    @Schema(description = "Пол", allowableValues = {"MALE", "FEMALE"})
    private Gender gender;

    @Schema(description = "Семейное положение", allowableValues = {"SINGLE", "MARRIED", "WIDOW_WIDOWER", "DIVORCED"})
    private MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев")
    private Integer dependentAmount;

    @Schema(description = "Дата выдачи паспорта")
    private LocalDate passportIssueDate;

    @Schema(description = "Кем выдан паспорт")
    private String passportIssueBranch;

    @Schema(description = "Информация о месте работы")
    private EmploymentDto employmentDto;

    @Schema(description = "Номер счета")
    private String accountNumber;
}
