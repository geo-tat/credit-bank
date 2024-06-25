package ru.neoflex.statement.client.deal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neoflex.statement.dto.LoanOfferDto;
import ru.neoflex.statement.dto.LoanStatementRequestDto;

import java.util.List;

@FeignClient(name = "deal", url = "${rest.deal.host}")
public interface DealApi {

    @PostMapping("/statement")
    List<LoanOfferDto> createStatement(@RequestBody LoanStatementRequestDto dto);

    @PostMapping("/offer/select")
    void selectOffer(@RequestBody LoanOfferDto dto);
}
