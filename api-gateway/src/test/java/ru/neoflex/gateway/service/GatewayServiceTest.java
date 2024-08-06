package ru.neoflex.gateway.service;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.gateway.client.deal.DealApi;
import ru.neoflex.gateway.client.statement.StatementApi;
import ru.neoflex.gateway.dto.FinishRegistrationRequestDto;
import ru.neoflex.gateway.dto.LoanOfferDto;
import ru.neoflex.gateway.dto.LoanStatementRequestDto;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GatewayServiceTest {
    @InjectMocks
    private GatewayServiceImpl service;
    @Mock
    private StatementApi statementFeignClient;

    @Mock
    private DealApi dealFeignClient;

    @Test
    void testCreateLoanOffers() {
        // Given
        LoanStatementRequestDto dto = new LoanStatementRequestDto();
        // When
        service.createLoanOffers(dto);
        // Then
        verify(statementFeignClient, times(1)).getLoanOffers(dto);
    }

    @Test
    void testCreateLoanOffersWhenException() {
        // Given
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();
        Request request = Request.create(Request.HttpMethod.POST, "/deal/document/{statementId}/code", Map.of(), new byte[0], null, new RequestTemplate());
        FeignException feignException = FeignException.errorStatus("sendDocument",
                Response.builder()
                        .status(500)
                        .reason("Internal Server Error")
                        .request(request)
                        .build());

        when(statementFeignClient.getLoanOffers(requestDto)).thenThrow(feignException);

        // When & Then
        assertThrows(FeignException.class, () -> service.createLoanOffers(requestDto));
    }

    @Test
    void testFinishRegistration() {
        // Given
        UUID statementId = UUID.randomUUID();
        FinishRegistrationRequestDto requestDto = new FinishRegistrationRequestDto();
        // When
        service.finishRegistration(statementId.toString(), requestDto);
        // Then
        verify(dealFeignClient, times(1)).finishRegistration(requestDto, statementId.toString());
    }

    @Test
    void testFinishRegistrationWhenException() {
        // Given
        String statementId = UUID.randomUUID().toString();
        FinishRegistrationRequestDto requestDto = new FinishRegistrationRequestDto();
        Request request = Request.create(Request.HttpMethod.POST, "/deal/document/{statementId}/code", Map.of(), new byte[0], null, new RequestTemplate());
        FeignException feignException = FeignException.errorStatus("sendDocument",
                Response.builder()
                        .status(500)
                        .reason("Internal Server Error")
                        .request(request)
                        .build());
        doThrow(feignException).when(dealFeignClient).finishRegistration(requestDto, statementId);

        // When & Then
        assertThrows(FeignException.class, () -> service.finishRegistration(statementId, requestDto));
        verify(dealFeignClient, times(1)).finishRegistration(requestDto, statementId);
    }

    @Test
    void testSelectOffer() {
        // Given
        LoanOfferDto offerDto = new LoanOfferDto();
        // When
        service.selectOffer(offerDto);
        // Then
        verify(statementFeignClient, times(1)).selectOffer(offerDto);
    }

    @Test
    void testSelectOfferWhenException() {
        // Given
        LoanOfferDto offerDto = new LoanOfferDto();
        Request request = Request.create(Request.HttpMethod.POST, "/deal/document/{statementId}/code", Map.of(), new byte[0], null, new RequestTemplate());
        FeignException feignException = FeignException.errorStatus("sendDocument",
                Response.builder()
                        .status(500)
                        .reason("Internal Server Error")
                        .request(request)
                        .build());
        doThrow(feignException).when(statementFeignClient).selectOffer(offerDto);
        // When & Then
        assertThrows(FeignException.class, () -> service.selectOffer(offerDto));
        verify(statementFeignClient, times(1)).selectOffer(offerDto);

    }


    @Test
    void testSendDocument() {
        // Given
        UUID statementId = UUID.randomUUID();
        // When
        service.sendDocument(statementId);
        // Then
        verify(dealFeignClient, times(1)).sendDocument(statementId);
    }

    @Test
    void testSendDocumentWhenException() {
        // Given
        UUID statementId = UUID.randomUUID();
        Request request = Request.create(Request.HttpMethod.POST, "/send", Map.of(), new byte[0], null, new RequestTemplate());
        FeignException feignException = FeignException.errorStatus("sendDocument",
                Response.builder()
                        .status(500)
                        .reason("Internal Server Error")
                        .request(request)
                        .build());

        doThrow(feignException).when(dealFeignClient).sendDocument(statementId);

        assertThrows(FeignException.class, () -> service.sendDocument(statementId));

        verify(dealFeignClient, times(1)).sendDocument(statementId);
    }

    @Test
    void testSignDocument() {
        // Given
        UUID statementId = UUID.randomUUID();
        boolean isSigned = true;
        // When
        service.signDocument(statementId, isSigned);
        // Then
        verify(dealFeignClient, times(1)).signDocument(statementId, isSigned);
    }

    @Test
    void testSignDocumentWhenException() {
        // Given
        UUID statementId = UUID.randomUUID();
        boolean isSigned = true;
        Request request = Request.create(Request.HttpMethod.POST, "/sign", Map.of(), new byte[0], null, new RequestTemplate());
        FeignException feignException = FeignException.errorStatus("signDocument",
                Response.builder()
                        .status(500)
                        .reason("Internal Server Error")
                        .request(request)
                        .build());

        doThrow(feignException).when(dealFeignClient).signDocument(statementId, isSigned);

        assertThrows(FeignException.class, () -> service.signDocument(statementId, isSigned));
        verify(dealFeignClient, times(1)).signDocument(statementId, isSigned);
    }

    @Test
    void testVerifyCode() {

        // Given
        UUID statementId = UUID.randomUUID();
        String code = "123";
        // When
        service.verifyCode(statementId, code);
        // Then
        verify(dealFeignClient, times(1)).verifyCode(statementId, code);
    }

    @Test
    void testVerifyCodeWhenException() {
        // Given
        UUID statementId = UUID.randomUUID();
        String code = "123";

        Request request = Request.create(Request.HttpMethod.POST, "/deal/document/{statementId}/code", Map.of(), new byte[0], null, new RequestTemplate());
        FeignException feignException = FeignException.errorStatus("sendDocument",
                Response.builder()
                        .status(500)
                        .reason("Internal Server Error")
                        .request(request)
                        .build());
        doThrow(feignException).when(dealFeignClient).verifyCode(statementId, code);
        // When
        // Then
        assertThrows(FeignException.class, () -> service.verifyCode(statementId, code));
        verify(dealFeignClient, times(1)).verifyCode(statementId, code);
    }


}