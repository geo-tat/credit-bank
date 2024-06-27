package ru.neoflex.statement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.statement.dto.LoanOfferDto;
import ru.neoflex.statement.dto.LoanStatementRequestDto;
import ru.neoflex.statement.exception.ErrorResponse;
import ru.neoflex.statement.service.StatementService;

import java.util.List;

@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
@Tag(name = "Микросервис заявка", description = "Подача заявки на кредит, осуществление прескоринга")
@Slf4j
public class StatementController {

    private final StatementService service;

    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Ошибки при валидации", responseCode = "400",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(description = "Расчёт возможных условий кредита. Request - LoanStatementRequestDto, response - List<LoanOfferDto>")
    @PostMapping
    List<LoanOfferDto> createStatement(@RequestBody @Valid LoanStatementRequestDto dto) {
        log.info("input LoanStatementRequestDto {}", dto);

        return service.makeStatement(dto);
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Ошибки при валидации", responseCode = "400",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(description = "Выбор одного из предложений. Request LoanOfferDto, response void")
    @PostMapping("/offer")
    void selectOffer(@RequestBody LoanOfferDto dto) {
        log.info("input LoanOfferDto {}", dto);
        service.selectOffer(dto);
    }
}
