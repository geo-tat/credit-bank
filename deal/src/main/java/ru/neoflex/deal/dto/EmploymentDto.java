package ru.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.deal.enums.EmploymentStatus;
import ru.neoflex.deal.enums.Position;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информация о занятости заявителя на кредит")
public class EmploymentDto {

    @Schema(description = "Статус занятости заявителя",
            allowableValues = {"EMPLOYED", "SELF_EMPLOYED", "UNEMPLOYED", "BUSINESS_OWNER"})
    private EmploymentStatus employmentStatus;

    @Schema(description = "ИНН работодателя", example = "7707083893")
    private String employerINN;


    @Schema(description = "Ежемесячная зарплата заявителя", example = "5000")
    private BigDecimal salary;

    @Schema(description = "Должность заявителя", allowableValues = {"""
            TOP_MANAGER,
            MID_MANAGER,
            WORKER,
            OWNER"""})
    private Position position;

    @Schema(description = "Общий опыт работы в месяцах", minimum = "0")
    private Integer workExperienceTotal;

    @Schema(description = "Текущий опыт работы в месяцах", minimum = "0")
    private Integer workExperienceCurrent;
}
