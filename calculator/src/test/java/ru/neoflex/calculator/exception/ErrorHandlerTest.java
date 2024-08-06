package ru.neoflex.calculator.exception;

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
import ru.neoflex.calculator.controller.CalculatorController;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.service.CalculatorServiceImpl;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculatorController.class)
public class ErrorHandlerTest {

    @MockBean
    private CalculatorServiceImpl service;

    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testHandleValidationException() throws Exception {
        // Given
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "defaultMessage");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        // Then
        mvc.perform(post("/calculator/calc")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testHandleCreditDeniedException() throws Exception {
        // Given
        ScoringDataDto dto = DtoBuilder.getScoringDataDto();
        LoanDeniedException exception = new LoanDeniedException("Credit denied");
        // When
        when(service.calculateCreditParameters(dto)).thenThrow(exception);
        // Then
        mvc.perform(post("/calculator/calc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("LoanDeniedException"))
                .andExpect(jsonPath("$.message").value("Credit denied"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}