package ru.neoflex.calculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.neoflex.calculator.DtoBuilder;
import ru.neoflex.calculator.dto.CreditDto;
import ru.neoflex.calculator.dto.LoanOfferDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.exception.LoanDeniedException;
import ru.neoflex.calculator.service.CalculatorServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalculatorController.class)
class CalculatorControllerTest {

    @MockBean
    private CalculatorServiceImpl service;

    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper;

    LoanStatementRequestDto requestDto;

    ScoringDataDto scoringDto;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        requestDto = DtoBuilder.getLoanStatementRequestDto();
        scoringDto = DtoBuilder.getScoringDataDto();
    }

    @Test
    void calculationOfPossibleLoanOffers() throws Exception {

        List<LoanOfferDto> loanOffers = List.of();


        when(service.calculateLoanOffers(any(LoanStatementRequestDto.class))).thenReturn(loanOffers);

        mvc.perform(post("/calculator/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loanOffers)));
    }

    @Test
    void testCalculateCreditParametres() throws Exception {

        CreditDto creditDto = CreditDto.builder().build();

        when(service.calculateCreditParameters(any(ScoringDataDto.class))).thenReturn(creditDto);

        mvc.perform(post("/calculator/calc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scoringDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(creditDto)));
    }
}