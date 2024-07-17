package ru.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.deal.enums.TopicType;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Сообщение на почту клиенту")
public class EmailMessage {
    @Schema(description = "id заявления")
    private UUID statementId;
    @Schema(description = "Адрес электронной почты клиента")
    private String address;
    @Schema(description = "Тема топика кафки", allowableValues = {"""
            FINISH_REGISTRATION,
            CREATE_DOCUMENTS,
            SEND_DOCUMENTS,
            SEND_SES,
            CREDIT_ISSUED,
            STATEMENT_DENIED"""})
    private TopicType theme;

}