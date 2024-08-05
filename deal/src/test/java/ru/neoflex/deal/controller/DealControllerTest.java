package ru.neoflex.deal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.deal.DtoBuilder;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.dto.ScoringDataDto;
import ru.neoflex.deal.service.DealServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DealController.class)
class DealControllerTest {

    @MockBean
    private DealServiceImpl service;

    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper;

    LoanStatementRequestDto requestDto;

    ScoringDataDto scoringDto;

    LoanOfferDto loanOfferDto;

    FinishRegistrationRequestDto finishRegistrationRequestDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        requestDto = DtoBuilder.getLoanStatementRequestDto();
        scoringDto = DtoBuilder.getScoringDataDto();
        loanOfferDto = DtoBuilder.getLoanOfferDto();
        finishRegistrationRequestDto = DtoBuilder.getFinishDto();
    }


    @Test
    void createStatement() throws Exception {
        List<LoanOfferDto> loanOffers = List.of();

        when(service.makeStatement(any(LoanStatementRequestDto.class))).thenReturn(loanOffers);

        mvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loanOffers)));
    }

    @Test
    void selectOffer() throws Exception {

        doNothing().when(service).selectOffer(loanOfferDto);

        mvc.perform(post("/deal/offer/select")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanOfferDto)))
                .andExpect(status().isOk());

    }

    @Test
    void finishRegistration() throws Exception {
        doNothing().when(service).finishRegistrationAndCalculation(loanOfferDto.getStatementId().toString(),
                finishRegistrationRequestDto);

        mvc.perform(post("/deal/calculate/{statementId}", loanOfferDto.getStatementId().toString())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(finishRegistrationRequestDto)))
                .andExpect(status().isOk());
    }
}