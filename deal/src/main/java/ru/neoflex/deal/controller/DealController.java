package ru.neoflex.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.exception.ErrorResponse;
import ru.neoflex.deal.service.interfaces.DealService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/deal")
@RequiredArgsConstructor
@Tag(name = "API Сделка", description = "Расчет кредита и регистрация")
public class DealController {

    private final DealService service;
    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Ошибки при валидации", responseCode = "400",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(description = "Расчёт возможных условий кредита. Request - LoanStatementRequestDto, response - List<LoanOfferDto>")
    @PostMapping("/statement")
    List<LoanOfferDto> createStatement(@RequestBody LoanStatementRequestDto dto) {
        log.info("method createStatement start processing");

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
    @PostMapping("/offer/select")
    void selectOffer(@RequestBody LoanOfferDto dto) {

        service.selectOffer(dto);
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Ошибки при валидации", responseCode = "400",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(description = "Завершение регистрации + полный подсчёт кредита." +
            "Request - FinishRegistrationRequestDto, param - String, response void.")
    @PostMapping("/calculate/{statementId}")
    void finishRegistration(@RequestBody FinishRegistrationRequestDto dto,
                            @PathVariable String statementId) {
        service.finishRegistrationAndCalculation(statementId, dto);
    }
}
