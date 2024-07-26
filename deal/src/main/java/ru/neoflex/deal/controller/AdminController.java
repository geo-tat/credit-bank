package ru.neoflex.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.deal.exception.ErrorResponse;
import ru.neoflex.deal.service.interfaces.StatementService;

import java.util.Collection;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/deal/admin")
@Tag(name = "API Aдминистрированиe")
public class AdminController {

    private final StatementService service;
    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Заявление не найдено", responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(description = "Поиск заявления по ID. Request UUID, response Statement")
    @GetMapping("/statement/{statementId}")
    Statement getStatementById(@PathVariable UUID statementId) {
        log.info("getStatementById - start. Id = {}", statementId.toString());
        Statement statement = service.getStatementById(statementId);
        log.info("get Statement: {}", statement);
        return statement;
    }
    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(description = "Получение списка всех заявлений. Request , response Collection<Statement>")
    @GetMapping("/statement")
    Collection<Statement> getAllStatements(@PageableDefault(sort = "creationDate", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("getAllStatements - start");
        Collection<Statement> statements = service.getAllStatements(pageable);
        log.info("statements: {}", statements);
        return statements;
    }
}
