package ru.neoflex.gateway.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.gateway.dto.FinishRegistrationRequestDto;
import ru.neoflex.gateway.dto.LoanOfferDto;
import ru.neoflex.gateway.dto.LoanStatementRequestDto;
import ru.neoflex.gateway.exception.ErrorResponse;
import ru.neoflex.gateway.service.GatewayService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "API Шлюз", description = "API для объединения запросов в один API")
public class GatewayController {

    private final GatewayService service;

    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Ошибки при валидации", responseCode = "400",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(description = "Расчёт возможных условий кредита. Request - LoanStatementRequestDto, response - List<LoanOfferDto>")
    @PostMapping("/statement")
    public List<LoanOfferDto> createLoanOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        log.info("input loanStatementRequestDto {}", loanStatementRequestDto);
       List<LoanOfferDto> offers = service.createLoanOffers(loanStatementRequestDto);
       log.info("output offers : {}", offers);
       return offers;
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Ошибки при валидации", responseCode = "400",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(description = "Выбор одного из предложений. Request LoanOfferDto, response void")
    @PostMapping("/statement/offer")
    public void selectLoanOffer(@RequestBody LoanOfferDto loanOfferDto) {
        log.info("loanOfferDto input: {}",loanOfferDto);
        service.selectOffer(loanOfferDto);
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
    @PostMapping("/statement/registration/{statementId}")
   public void finishRegistration(@RequestBody FinishRegistrationRequestDto dto,
                            @PathVariable String statementId) {
        log.info("finishRegistrationDto: {}", dto);
        service.finishRegistration(statementId,dto);
    }
    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(description = "Формирование пакета документов для клиента")
    @PostMapping("/deal/document/{statementId}/send")
   public void sendDocument(@PathVariable UUID statementId) {
        service.sendDocument(statementId);
    }
    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(description = "Согласи или отказ предложения банка о кредите")
    @PostMapping("/deal/document/{statementId}/sign")
   public void signDocument(@PathVariable UUID statementId, @RequestParam boolean isSigned) {
        service.signDocument(statementId,isSigned);
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(description = "Верификация проверочного кода")
    @PostMapping("/deal/document/{statementId}/code")
   public void verifyCode(@PathVariable UUID statementId, @RequestParam String code) {
        service.verifyCode(statementId,code);
    }


}
