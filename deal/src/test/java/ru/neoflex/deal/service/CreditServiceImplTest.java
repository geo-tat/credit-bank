package ru.neoflex.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.deal.DtoBuilder;
import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.entity.Credit;
import ru.neoflex.deal.mapper.DealMapper;
import ru.neoflex.deal.repository.CreditRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditServiceImplTest {


    @Mock
    private CreditRepository creditRepository;

    @InjectMocks
    private CreditServiceImpl creditService;

    CreditDto creditDto;

    @Test
    public void testCreateCredit() {
        // Given
        creditDto = DtoBuilder.getCreditDto();
        Credit creditToSave = DealMapper.initializeCredit(creditDto);
        Credit savedCredit = new Credit();
        savedCredit.setId(creditToSave.getId());

        // When
        when(creditRepository.save(any(Credit.class))).thenReturn(savedCredit);
        Credit result = creditService.createCredit(creditDto);

        // Then
        assertEquals(savedCredit.getId(), result.getId());

        verify(creditRepository).save(any(Credit.class));
    }

}