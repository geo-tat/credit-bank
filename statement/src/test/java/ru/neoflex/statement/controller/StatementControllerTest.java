package ru.neoflex.statement.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.statement.DtoBuilder;
import ru.neoflex.statement.dto.LoanOfferDto;
import ru.neoflex.statement.dto.LoanStatementRequestDto;
import ru.neoflex.statement.exception.LoanDeniedException;
import ru.neoflex.statement.service.StatementServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatementController.class)
class StatementControllerTest {

    @MockBean
    private StatementServiceImpl service;

    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper;

    LoanStatementRequestDto requestDto;


    LoanOfferDto loanOfferDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        requestDto = DtoBuilder.getLoanStatementRequestDto();
        loanOfferDto = DtoBuilder.getLoanOfferDto();

    }


    @Test
    void createStatement() throws Exception {
        List<LoanOfferDto> loanOffers = List.of();

        when(service.makeStatement(any(LoanStatementRequestDto.class))).thenReturn(loanOffers);

        mvc.perform(post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loanOffers)));
    }

    @Test
    void selectOffer() throws Exception {

        doNothing().when(service).selectOffer(loanOfferDto);

        mvc.perform(post("/statement/offer")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanOfferDto)))
                .andExpect(status().isOk());

    }

    @Test
    void makeStatementWhenInvalidName() throws Exception {
        // Given
        requestDto.setFirstName("A"); // Invalid first name

        // When & Then
        mvc.perform(post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.sourceClass").exists())
                .andExpect(jsonPath("$.message").value("First name must be between 2 and 30 characters"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void makeStatementWhenInvalidAge() throws Exception {
        // Given
        requestDto.setBirthdate(LocalDate.of(2015, 1, 1));

        // When & Then
        mvc.perform(post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.sourceClass").exists())
                .andExpect(jsonPath("$.message").value("Must be more or 18 years old"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void makeStatementWhenAmountLess30000() throws Exception {
        // Given
        requestDto.setAmount(BigDecimal.valueOf(1000));

        // When & Then
        mvc.perform(post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.sourceClass").exists())
                .andExpect(jsonPath("$.message").value("Loan amount must be greater than or equal to 30000"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void makeStatementWhenTermLess6Month() throws Exception {
        // Given
        requestDto.setTerm(1);

        // When & Then
        mvc.perform(post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.sourceClass").exists())
                .andExpect(jsonPath("$.message").value("Loan term must be greater than or equal to 6"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testMakeStatementThrowsLoanDeniedException() throws Exception {
        // Given & When
        when(service.makeStatement(requestDto)).thenThrow(new LoanDeniedException("Credit denied"));


        mvc.perform(post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.sourceClass").exists())
                .andExpect(jsonPath("$.message").value("Credit denied"))
                .andExpect(jsonPath("$.timestamp").exists());

    }

}


