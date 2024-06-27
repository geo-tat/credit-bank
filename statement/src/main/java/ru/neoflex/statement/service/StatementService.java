package ru.neoflex.statement.service;

import ru.neoflex.statement.dto.LoanOfferDto;
import ru.neoflex.statement.dto.LoanStatementRequestDto;

import java.util.List;

public interface StatementService {
    List<LoanOfferDto> makeStatement(LoanStatementRequestDto dto);

    void selectOffer(LoanOfferDto dto);
}
