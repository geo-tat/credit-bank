package ru.neoflex.statement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.statement.client.deal.DealApi;
import ru.neoflex.statement.dto.LoanOfferDto;
import ru.neoflex.statement.dto.LoanStatementRequestDto;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatementServiceImpl implements StatementService {
    private final DealApi feignClient;

    @Override
    public List<LoanOfferDto> makeStatement(LoanStatementRequestDto dto) {
        List<LoanOfferDto> list = feignClient.createStatement(dto);
        for (int i = 1; i <= list.size(); i++) {
            log.info("output offer #{} {}", i, list.get(i - 1));
        }
        return list;
    }

    @Override
    public void selectOffer(LoanOfferDto dto) {
        feignClient.selectOffer(dto);
        log.info("offer selected");
    }
}
