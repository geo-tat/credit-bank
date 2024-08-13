package ru.neoflex.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.deal.exception.ErrorResponse;
import ru.neoflex.deal.service.interfaces.ClientService;
import ru.neoflex.deal.service.interfaces.StatementService;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/deal/admin")
@Tag(name = "API Aдминистрированиe")
public class AdminController {

    private final StatementService statementService;

    private final ClientService clientService;

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
        Statement statement = statementService.getStatementById(statementId);
        log.info("get Statement: {}", statement);
        return statement;
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(description = "Получение списка всех заявлений. Request , response Page<Statement>")
    @GetMapping("/statement")
    Page<Statement> getAllStatements(@PageableDefault(sort = "creationDate", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("getAllStatements - start");
        Page<Statement> statements = statementService.getAllStatements(pageable);
        log.info("statements: {}", statements);
        return statements;
    }
    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Клиент не найден", responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(description = "Получение списка всех заявлений конкретного клиента. Request , response Page<Statement>")
    @GetMapping("/{clientId}/statement")
    Page<Statement> getAllStatementsByClientId(@PathVariable UUID clientId,
                                                     @PageableDefault(sort = "creationDate", direction = Sort.Direction.ASC) Pageable pageable) {

        log.info("getAllStatementsByClientId - start");
        Page<Statement> statements = statementService.getAllStatementsByClientId(clientId,pageable);
        log.info("statementsByClient: {}", statements);
        return statements;
    }
    @ApiResponses(value = {
            @ApiResponse(description = "Успешный ответ на запрос", responseCode = "200"),
            @ApiResponse(description = "Internal server error", responseCode = "500",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})
    })
    @Operation(description = "Получение списка всех клиентов. Request , response Page<Statement>")
    @GetMapping("/client")
    Page<Client> getAllClients( @PageableDefault(sort = "creationDate", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Client> clients = clientService.getAllClients(pageable);
        log.info("clients: {}", clients);
        return clients;
    }
}
