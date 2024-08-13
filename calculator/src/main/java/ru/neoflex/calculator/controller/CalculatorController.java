package ru.neoflex.calculator.controller;


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
import ru.neoflex.calculator.dto.CreditDto;
import ru.neoflex.calculator.dto.LoanOfferDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.exception.ErrorResponse;
import ru.neoflex.calculator.service.CalculatorService;

import java.util.List;


@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
@Tag(name = "Кредитный калькулятор", description = "Расчет кредита на основе входных данных")
@Slf4j
public class CalculatorController {

    private final CalculatorService service;

    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Ошибки при валидации", responseCode = "400",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("/offers")
    @Operation(summary = "Расчёт возможных условий кредита")
    List<LoanOfferDto> calculationOfPossibleLoanOffers(@RequestBody @Valid LoanStatementRequestDto dto) {
        log.info("Тело запроса: {}", dto);
        return service.calculateLoanOffers(dto);
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Ошибки при валидации", responseCode = "400",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(description = "Internal server error",
                    responseCode = "500", content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("/calc")
    @Operation(summary = "Валидация присланных данных + скоринг данных + полный расчет параметров кредита")
    CreditDto calculateCreditParametres(@RequestBody @Valid ScoringDataDto dto) {
        log.info("Тело запроса: {}", dto);
        return service.calculateCreditParameters(dto);
    }
}
