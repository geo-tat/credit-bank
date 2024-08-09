package ru.neoflex.deal.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.deal.exception.SesCodeIsNotValidException;
import ru.neoflex.deal.exception.StatementStatusIsNotValidException;
import ru.neoflex.deal.service.DocumentServiceImpl;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentController.class)
class DocumentControllerTest {


    @MockBean
    private DocumentServiceImpl service;

    @Autowired
    private MockMvc mvc;

    private final UUID statementId = UUID.randomUUID();

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
    void sesCodeTestInvalid() throws Exception {
        // Given
        String code = "invalid_code";
        SesCodeIsNotValidException exception = new SesCodeIsNotValidException("Invalid ses_code");
        // When
        doThrow(exception).when(service).verifyCode(statementId, code);

        // Then
        mvc.perform(post("/deal/document/{statementId}/code", statementId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("code",code))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("SesCodeIsNotValidException"))
                .andExpect(jsonPath("$.message").value("Invalid ses_code"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void signDocumentTestWhenStatementStatusException() throws Exception {
        // Given
        boolean isSigned = true;
        StatementStatusIsNotValidException exception = new StatementStatusIsNotValidException("Statement status invalid");
        // When
        doThrow(exception).when(service).signDocument(statementId, isSigned);
        // Then
        mvc.perform(post("/deal/document/{statementId}/sign", statementId.toString())
                        .param("isSigned", String.valueOf(isSigned))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("StatementStatusIsNotValidException"))
                .andExpect(jsonPath("$.message").value("Statement status invalid"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

}
