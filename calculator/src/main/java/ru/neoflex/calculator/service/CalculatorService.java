package ru.neoflex.calculator.service;


import ru.neoflex.calculator.dto.CreditDto;
import ru.neoflex.calculator.dto.LoanOfferDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.ScoringDataDto;

import java.util.List;

public interface CalculatorService {

    /**
     * Рассчитывает возможные условия кредита на основе данных запроса.
     *
     * @param loanStatementRequestDto данные запроса для расчета условий кредита
     * @return список возможных кредитных предложений
     */
    List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto loanStatementRequestDto);

    /**
     * Выполняет полный расчет параметров кредита, включая валидацию и скоринг данных.
     *
     * @param scoringDataDto данные для скоринга и расчета параметров кредита
     * @return рассчитанные параметры кредита
     */
    CreditDto calculateCreditParameters(ScoringDataDto scoringDataDto);
}
