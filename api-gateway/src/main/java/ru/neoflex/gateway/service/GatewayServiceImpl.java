package ru.neoflex.gateway.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.gateway.client.deal.DealApi;
import ru.neoflex.gateway.client.statement.StatementApi;
import ru.neoflex.gateway.dto.FinishRegistrationRequestDto;
import ru.neoflex.gateway.dto.LoanOfferDto;
import ru.neoflex.gateway.dto.LoanStatementRequestDto;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GatewayServiceImpl implements GatewayService {

    private final DealApi dealFeign;

    private final StatementApi statementFeign;

    @Override
    public List<LoanOfferDto> createLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        List<LoanOfferDto> offers;
        try {
            offers = statementFeign.getLoanOffers(loanStatementRequestDto);

        } catch (FeignException e) {
            log.error("Error creating offers: {}", e.getMessage(), e);
            throw e;
        }

        return offers;
    }

    @Override
    public void selectOffer(LoanOfferDto loanOfferDto) {
        try {

            statementFeign.selectOffer(loanOfferDto);
        } catch (FeignException e) {
            log.error("Error selecting offer: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void finishRegistration(String statementId, FinishRegistrationRequestDto dto) {
        try {
            dealFeign.finishRegistration(dto, statementId);
        } catch (FeignException e) {
            log.error("Error finishing registration: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void sendDocument(UUID statementId) {
        try {
            dealFeign.sendDocument(statementId);
        } catch (FeignException e) {
            log.error("Error sending document: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void signDocument(UUID statementId, boolean isSigned) {
        try {
            dealFeign.signDocument(statementId, isSigned);
        } catch (FeignException e) {
            log.error("Error signing document: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void verifyCode(UUID statementId, String code) {
        try {
            dealFeign.verifyCode(statementId, code);
        } catch (FeignException e) {
            log.error("Error verifying code: {}", e.getMessage(), e);
            throw e;
        }
    }
}
