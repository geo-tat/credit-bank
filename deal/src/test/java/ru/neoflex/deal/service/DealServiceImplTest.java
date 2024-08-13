package ru.neoflex.deal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.deal.DtoBuilder;
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
import ru.neoflex.deal.exception.OfferAlreadySelectedException;
import ru.neoflex.deal.mapper.DealMapper;
import ru.neoflex.deal.service.interfaces.ClientService;
import ru.neoflex.deal.service.interfaces.CreditService;
import ru.neoflex.deal.service.interfaces.DocumentService;
import ru.neoflex.deal.service.interfaces.StatementService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealServiceImplTest {

    @InjectMocks
    private DealServiceImpl dealService;

    @Mock
    private ClientService clientService;

    @Mock
    private StatementService statementService;

    @Mock
    private DocumentService documentService;

    @Mock
    private CreditService creditService;

    @Mock
    private CalculatorApi feinClient;

    @Mock
    private DealMapper dealMapper;

    private LoanStatementRequestDto requestDto;
    private LoanOfferDto loanOfferDto;
    private FinishRegistrationRequestDto finishRegistrationRequestDto;
    private UUID statementId;

    @BeforeEach
    void setUp() {
        requestDto = DtoBuilder.getLoanStatementRequestDto();
        loanOfferDto = DtoBuilder.getLoanOfferDto();
        finishRegistrationRequestDto = DtoBuilder.getFinishDto();
        statementId = UUID.randomUUID();
    }

    @Test
    void testMakeStatement() {
        // Given
        Client client = Client.builder().build();
        Statement statement = Statement.builder().build();
        statement.setId(statementId);
        List<LoanOfferDto> loanOffers = List.of(new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto());

        // When
        when(clientService.createClient(requestDto)).thenReturn(client);
        when(statementService.createStatement(client, requestDto)).thenReturn(statement);
        when(feinClient.calculationOfPossibleLoanOffers(requestDto)).thenReturn(loanOffers);

        List<LoanOfferDto> result = dealService.makeStatement(requestDto);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(4);
        assertThat(result.get(0).getStatementId()).isEqualTo(statementId);
        assertThat(result.get(1).getStatementId()).isEqualTo(statementId);

        verify(clientService).createClient(requestDto);
        verify(statementService).createStatement(client, requestDto);
        verify(feinClient).calculationOfPossibleLoanOffers(requestDto);
    }

    @Test
    void testSelectOffer() {
        // Given
        loanOfferDto.setStatementId(statementId);
        Statement statement = Statement.builder().build();
        statement.setId(statementId);
        statement.setApplicationStatus(ApplicationStatus.PREAPPROVAL);
        Client client = DtoBuilder.getClient(finishRegistrationRequestDto,requestDto);
        statement.setClient(client);
        loanOfferDto.setStatementId(statementId);

        // When
        when(statementService.getStatementById(statementId)).thenReturn(statement);
        dealService.selectOffer(loanOfferDto);

        // Then
        verify(statementService).getStatementById(statementId);
        verify(statementService).updateStatement(statement);
        assertThat(statement.getAppliedOffer()).isEqualTo(loanOfferDto);
    }

    @Test
    void testSelectOfferAlreadyChosen() {
        // Given
        loanOfferDto.setStatementId(statementId);
        Statement statement = Statement.builder()
                .id(statementId)
                .applicationStatus(ApplicationStatus.APPROVED)
                .build();

        // When
        when(statementService.getStatementById(statementId)).thenReturn(statement);

        // Then
        assertThatThrownBy(() -> dealService.selectOffer(loanOfferDto))
                .isInstanceOf(OfferAlreadySelectedException.class)
                .hasMessage("Кредитное предложение уже выбрано");

        verify(statementService).getStatementById(statementId);
        verify(statementService, never()).updateStatement(any());
    }

    @Test
    void testFinishRegistrationAndCalculation() {
        // Given
        List<StatementStatusHistoryDto> statusHistory = new ArrayList<>();
        Client client = DtoBuilder.getClient(finishRegistrationRequestDto, requestDto);
        Statement statement = Statement.builder()
                .client(client)
                .appliedOffer(loanOfferDto)
                .applicationStatus(ApplicationStatus.PREAPPROVAL)
                .creationDate(LocalDateTime.now())
                .statusHistory(statusHistory)
                .build();
        loanOfferDto.setStatementId(statementId);
        CreditDto creditDto = CreditDto.builder().build();
        Credit credit = Credit.builder().build();

        // When
        when(statementService.getStatementById(statementId)).thenReturn(statement);
        ScoringDataDto scoringDataDto = DealMapper.initializeScoringDataDto(finishRegistrationRequestDto, statement);
        when(feinClient.calculateCreditParametres(scoringDataDto)).thenReturn(creditDto);
        when(creditService.createCredit(creditDto)).thenReturn(credit);
        when(clientService.finalUpdateClient(statement.getClient(), finishRegistrationRequestDto)).thenReturn(client);

        dealService.finishRegistrationAndCalculation(statementId.toString(), finishRegistrationRequestDto);

        // Then
        verify(statementService).getStatementById(statementId);
        verify(feinClient).calculateCreditParametres(scoringDataDto);
        verify(creditService).createCredit(creditDto);
        verify(clientService).finalUpdateClient(statement.getClient(), finishRegistrationRequestDto);
        verify(statementService).updateStatement(statement);

        assertThat(statement.getCredit()).isEqualTo(credit);
        assertThat(statement.getApplicationStatus()).isEqualTo(ApplicationStatus.CC_APPROVED);
        assertThat(statement.getStatusHistory()).hasSize(1);
        assertThat(statement.getStatusHistory().get(0).getStatus()).isEqualTo(ApplicationStatus.CC_APPROVED);
    }
}
