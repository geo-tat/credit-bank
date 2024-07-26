package ru.neoflex.gateway.client.statement;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neoflex.gateway.dto.LoanOfferDto;
import ru.neoflex.gateway.dto.LoanStatementRequestDto;

import java.util.List;

@FeignClient(name = "statement", url = "${rest.statement.host}")
public interface StatementApi {
    @PostMapping(value = "${rest.statement.method.calculate-offers}")
    List<LoanOfferDto> createStatement(@RequestBody LoanStatementRequestDto dto);

    @PostMapping(value = "${rest.statement.method.select-offer}")
    void selectOffer(@RequestBody LoanOfferDto dto);
}
