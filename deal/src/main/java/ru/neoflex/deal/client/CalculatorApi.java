package ru.neoflex.deal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.dto.ScoringDataDto;

import java.util.List;


@FeignClient(name = "calculator", url = "${rest.calculator.host}")
public interface CalculatorApi {

    @PostMapping("/offers")
    List<LoanOfferDto> calculationOfPossibleLoanOffers(@RequestBody LoanStatementRequestDto dto);

    @PostMapping("/calc")
    CreditDto calculateCreditParametres(@RequestBody ScoringDataDto dto);
}
