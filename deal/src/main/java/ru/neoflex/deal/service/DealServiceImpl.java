package ru.neoflex.deal.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.deal.client.CalculatorApi;
import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.dto.ScoringDataDto;
import ru.neoflex.deal.dto.StatementStatusHistoryDto;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.entity.Credit;
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.deal.enums.ApplicationStatus;
import ru.neoflex.deal.enums.ChangeType;
import ru.neoflex.deal.exception.OfferAlreadySelectedException;
import ru.neoflex.deal.mapper.DealMapper;
import ru.neoflex.deal.service.interfaces.ClientService;
import ru.neoflex.deal.service.interfaces.CreditService;
import ru.neoflex.deal.service.interfaces.DealService;
import ru.neoflex.deal.service.interfaces.DocumentService;
import ru.neoflex.deal.service.interfaces.StatementService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealServiceImpl implements DealService {

    private final CalculatorApi feinClient;
    private final ClientService clientService;
    private final StatementService statementService;
    private final CreditService creditService;
    private final DocumentService documentService;

    @Transactional
    @Override
    public List<LoanOfferDto> makeStatement(LoanStatementRequestDto dto) {
        log.info("makeStatement method processing");

        Client client = clientService.createClient(dto);
        Statement statement = statementService.createStatement(client, dto);
        List<LoanOfferDto> list = feinClient.calculationOfPossibleLoanOffers(dto);
        log.debug("received loan offers from MS-calculator");

        return list.stream()
                .peek(offer -> offer.setStatementId(statement.getId()))
                .toList();
    }

    @Transactional
    @Override
    public void selectOffer(LoanOfferDto dto) {
        log.info("selectOffer method processing");

        Statement statement = statementService.getStatementById(dto.getStatementId());

        if (!statement.getApplicationStatus().equals(ApplicationStatus.PREAPPROVAL)) {
            log.error("offer already chosen");
            throw new OfferAlreadySelectedException("Кредитное предложение уже выбрано");
        }
        statement.setAppliedOffer(dto);
        log.debug("set applied offer in statement {}", dto);

        statementService.updateStatement(statement);

        documentService.finishRegistration(statement.getId(), statement.getClient().getEmail());
    }


    @Override
    @Transactional
    public void finishRegistrationAndCalculation(String statementId, FinishRegistrationRequestDto dto) {
        log.info("finish registration and calculation method processing");
        UUID statmentUUID = UUID.fromString(statementId);

        Statement statement = statementService.getStatementById(statmentUUID);

        ScoringDataDto scoringDataDto = DealMapper.initializeScoringDataDto(dto, statement);
        try {
            CreditDto creditDto = feinClient.calculateCreditParametres(scoringDataDto);
            log.debug("received creditDto from MS-calculator {}", creditDto);
            Credit credit = creditService.createCredit(creditDto);
            Client client = clientService.finalUpdateClient(statement.getClient(), dto);

            statement.setCredit(credit);
            statement.setApplicationStatus(ApplicationStatus.CC_APPROVED);
            statement.getStatusHistory().add(StatementStatusHistoryDto.builder()
                    .status(ApplicationStatus.CC_APPROVED)
                    .time(LocalDateTime.now())
                    .changeType(ChangeType.AUTOMATIC)
                    .build());
            statement.setClient(client);

            statementService.updateStatement(statement);

            documentService.createDocument(statement.getId(), statement.getClient().getEmail());
        } catch (Exception e) {
            statement.setApplicationStatus(ApplicationStatus.CC_DENIED);
            statement.getStatusHistory().add(StatementStatusHistoryDto.builder()
                    .status(ApplicationStatus.CC_DENIED)
                    .time(LocalDateTime.now())
                    .changeType(ChangeType.AUTOMATIC)
                    .build());
            statementService.updateStatement(statement);
            documentService.sendDenied(statmentUUID, statement.getClient().getEmail());
        }

    }
}
