package ru.neoflex.statement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.statement.DtoBuilder;
import ru.neoflex.statement.client.deal.DealApi;
import ru.neoflex.statement.dto.LoanOfferDto;
import ru.neoflex.statement.dto.LoanStatementRequestDto;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatementServiceImplTest {

    @InjectMocks
    private StatementServiceImpl service;

    @Mock
    private DealApi feignClient;

    private LoanStatementRequestDto requestDto;

    private LoanOfferDto loanOfferDto;

    @BeforeEach
    void setUp() {
        requestDto = DtoBuilder.getLoanStatementRequestDto();

        loanOfferDto = DtoBuilder.getLoanOfferDto();
    }


    @Test
    void testMakeStatement() {
        // Given
        List<LoanOfferDto> loanOffers = List.of(new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto());

        // When

        when(feignClient.createStatement(requestDto)).thenReturn(loanOffers);

        List<LoanOfferDto> result = service.makeStatement(requestDto);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(loanOffers.size());
        assertEquals(loanOffers, result);

        verify(feignClient).createStatement(requestDto);
    }

    @Test
    void testSelectOffer() {
        // Given

        // When
        service.selectOffer(loanOfferDto);

        // Then
        verify(feignClient, times(1)).selectOffer(loanOfferDto);
    }
}