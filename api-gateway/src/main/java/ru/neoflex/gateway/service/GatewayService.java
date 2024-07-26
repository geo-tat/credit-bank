package ru.neoflex.gateway.service;

import ru.neoflex.gateway.dto.FinishRegistrationRequestDto;
import ru.neoflex.gateway.dto.LoanOfferDto;
import ru.neoflex.gateway.dto.LoanStatementRequestDto;

import java.util.List;
import java.util.UUID;

public interface GatewayService {
    List<LoanOfferDto> createLoanOffers(LoanStatementRequestDto loanStatementRequestDto);

    void selectOffer(LoanOfferDto loanOfferDto);

    void finishRegistration(String statementId, FinishRegistrationRequestDto dto);

    void sendDocument(UUID statementId);

    void signDocument(UUID statementId, boolean isSigned);

    void verifyCode(UUID statementId, String code);
}
