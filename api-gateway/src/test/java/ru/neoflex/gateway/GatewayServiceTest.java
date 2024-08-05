package ru.neoflex.gateway;

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
import ru.neoflex.gateway.service.GatewayServiceImpl;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GatewayServiceTest {
    @InjectMocks
    private GatewayServiceImpl service;
    @Mock
    private StatementApi statementFeignClient;

    @Mock
    private DealApi dealFeignClient;

    @Test
    void testFinishRegistration() {
        UUID statementId = UUID.randomUUID();
        FinishRegistrationRequestDto requestDto = new FinishRegistrationRequestDto();

        service.finishRegistration(statementId.toString(), requestDto);

        verify(dealFeignClient, times(1)).finishRegistration(requestDto, statementId.toString());
    }

    @Test
    void testSelectOffer() {
        LoanOfferDto offerDto = new LoanOfferDto();

        service.selectOffer(offerDto);

        verify(statementFeignClient, times(1)).selectOffer(offerDto);
    }

    @Test
    void testCreateStatement() {
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();
        service.createLoanOffers(requestDto);
        verify(statementFeignClient, times(1)).getLoanOffers(requestDto);
    }

    @Test
    void testSendDocument() {
        UUID statementId = UUID.randomUUID();

        service.sendDocument(statementId);

        verify(dealFeignClient, times(1)).sendDocument(statementId);
    }

    @Test
    void testSignDocument() {
        UUID statementId = UUID.randomUUID();
        boolean isSigned = true;
        service.signDocument(statementId, isSigned);

        verify(dealFeignClient, times(1)).signDocument(statementId, isSigned);
    }

    @Test
    void testVerifyCode() {
        UUID statementId = UUID.randomUUID();
        String code = "123";

        service.verifyCode(statementId, code);

        verify(dealFeignClient, times(1)).verifyCode(statementId, code);
    }
}