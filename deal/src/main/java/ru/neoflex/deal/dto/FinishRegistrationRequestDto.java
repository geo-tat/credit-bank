package ru.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.deal.enums.Gender;
import ru.neoflex.deal.enums.MaritalStatus;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Заявление на окончательное оформление кредита")
public class FinishRegistrationRequestDto {

    @Enumerated(EnumType.STRING)
    @Schema(description = "Пол", allowableValues = {"MALE", "FEMALE"})
    private Gender gender;

    @Schema(description = "Семейное положение", allowableValues = {"SINGLE", "MARRIED", "WIDOW_WIDOWER", "DIVORCED"})
    @Enumerated(EnumType.STRING)
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
