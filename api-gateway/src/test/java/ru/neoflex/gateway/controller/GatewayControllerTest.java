package ru.neoflex.gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.gateway.dto.FinishRegistrationRequestDto;
import ru.neoflex.gateway.dto.LoanOfferDto;
import ru.neoflex.gateway.dto.LoanStatementRequestDto;
import ru.neoflex.gateway.exception.ErrorResponse;
import ru.neoflex.gateway.service.GatewayServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GatewayController.class)
class GatewayControllerTest {
    @MockBean
    private GatewayServiceImpl service;

    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper;

    private final UUID statementId = UUID.randomUUID();


    private LoanStatementRequestDto requestDto;

    private LoanOfferDto loanOfferDto;

    @BeforeEach
    void setUp() {
        requestDto = LoanStatementRequestDto.builder().build();
        loanOfferDto = LoanOfferDto.builder().build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Test
    void createLoanOffersTest() throws Exception {
        List<LoanOfferDto> loanOffers = List.of();

        when(service.createLoanOffers(any(LoanStatementRequestDto.class))).thenReturn(loanOffers);

        mvc.perform(post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loanOffers)));
    }

    @Test
    void selectLoanOfferTest() throws Exception {
        doNothing().when(service).selectOffer(loanOfferDto);

        mvc.perform(post("/statement/offer")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loanOfferDto)))
                .andExpect(status().isOk());
    }

    @Test
    void finishRegistrationTest() throws Exception {
        doNothing().when(service).finishRegistration(UUID.randomUUID().toString(),
                new FinishRegistrationRequestDto());

        mvc.perform(post("/statement/registration/{statementId}", UUID.randomUUID().toString())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new FinishRegistrationRequestDto())))
                .andExpect(status().isOk());
    }

    @Test
    void sendDocumentTest() throws Exception {
        mvc.perform(post("/deal/document/{statementId}/send", statementId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).sendDocument(statementId);
    }

    @Test
    void signDocumentTest() throws Exception {
        boolean isSigned = true;
        mvc.perform(post("/deal/document/{statementId}/sign", statementId.toString())
                        .param("isSigned", String.valueOf(isSigned))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).signDocument(statementId, isSigned);
    }

    @Test
    void sesCodeTest() throws Exception {
        String code = "1234";
        mvc.perform(post("/deal/document/{statementId}/code", statementId.toString())
                        .param("code", code)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).verifyCode(statementId, code);
    }

    @Test
    void testHandleFeignException() throws Exception {
        // Given
        ErrorResponse response = new ErrorResponse("InternalServerError",
                "Optional[ru.neoflex.gateway.service.GatewayServiceImpl.createLoanOffers(GatewayServiceImpl.java:29)]",
                "[500 Internal Server Error] during [POST] to [/statement] [create loan offers]: []",
                LocalDateTime.now());
        Request request = Request.create(Request.HttpMethod.POST, "/statement", Map.of(), new byte[0], null, new RequestTemplate());
        FeignException feignException = FeignException.errorStatus("create loan offers",
                Response.builder()
                        .status(500)
                        .reason("Internal Server Error")
                        .request(request)
                        .build());
        // When
        when(service.createLoanOffers(new LoanStatementRequestDto())).thenThrow(feignException);
        // Then
        mvc.perform(post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoanStatementRequestDto())))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}