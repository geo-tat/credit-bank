package ru.neoflex.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.deal.exception.ErrorResponse;
import ru.neoflex.deal.service.interfaces.DocumentService;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/deal/document/{statementId}")
@RequiredArgsConstructor
@Tag(name = "МС Сделка", description = "Формирование сообщения для МС Кредитное досье")
public class DocumentController {

    private final DocumentService documentService;

    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(description = "Формирование пакета документов для клиента")
    @PostMapping("/send")
    void sendDocument(@PathVariable UUID statementId) {
        documentService.sendDocument(statementId);
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(description = "Согласи или отказ предложения банка о кредите")
    @PostMapping("/sign")
    void signDocument(@PathVariable UUID statementId, @RequestParam boolean isSigned) {
        documentService.signDocument(statementId, isSigned);
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(description = "Верификация проверочного кода")
    @PostMapping("/code")
    void sesCode(@PathVariable UUID statementId, @RequestParam String code) {
        documentService.verifyCode(statementId, code);
    }

}
