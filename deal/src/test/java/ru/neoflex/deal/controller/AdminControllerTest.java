package ru.neoflex.deal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.deal.exception.ErrorResponse;
import ru.neoflex.deal.service.StatementServiceImpl;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {


    @MockBean
    private StatementServiceImpl service;

    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper;

    private final UUID statementId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getStatementByIdTest() throws Exception {
        Statement statement = Statement.builder().build();
        when(service.getStatementById(statementId)).thenReturn(statement);

        mvc.perform(get("/deal/admin/statement/{statementId}", statementId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(statement)));
    }

    @Test
    void testGetStatementByIdNotFound() throws Exception {
        // Given: simulate endpoint that throws EntityNotFoundException
        String id = UUID.randomUUID().toString();
        EntityNotFoundException exception = new EntityNotFoundException("Entity not found");
        when(service.getStatementById(any())).thenThrow(exception);
        MvcResult result = mvc.perform(get("/deal/admin/statement/{statementId}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exception").value("EntityNotFoundException"))
                .andExpect(jsonPath("$.sourceClass").exists())
                .andExpect(jsonPath("$.message").value("Entity not found"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(responseBody, ErrorResponse.class);
        assert errorResponse.getException().equals("EntityNotFoundException");
        assert errorResponse.getMessage().equals("Entity not found");

    }

    @Test
    void testGetAllStatements_Success() throws Exception {
        Statement statement1 = Statement.builder().build();
        Statement statement2 = Statement.builder().build();
        Collection<Statement> statements = List.of(statement2, statement1);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("creationDate").ascending());
        when(service.getAllStatements(pageable)).thenReturn(statements);

        mvc.perform(get("/deal/admin/statement")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "creationDate,asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}