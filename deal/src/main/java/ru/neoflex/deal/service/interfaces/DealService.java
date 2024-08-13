package ru.neoflex.deal.service.interfaces;

import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;

import java.util.List;

public interface DealService {
    List<LoanOfferDto> makeStatement(LoanStatementRequestDto dto);

    void selectOffer(LoanOfferDto dto);

    void finishRegistrationAndCalculation(String statementId, FinishRegistrationRequestDto dto);
}
