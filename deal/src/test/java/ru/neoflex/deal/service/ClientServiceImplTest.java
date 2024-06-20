package ru.neoflex.deal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.deal.DtoBuilder;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.mapper.DealMapper;
import ru.neoflex.deal.repository.ClientRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    LoanStatementRequestDto loanStatementRequestDto;

    Client savedClient;

    FinishRegistrationRequestDto finishRegistrationRequestDto;

    @BeforeEach
    public void setUp() {
        loanStatementRequestDto = DtoBuilder.getLoanStatementRequestDto();

        savedClient = Client.builder()
                .firstName(loanStatementRequestDto.getFirstName())
                .lastName(loanStatementRequestDto.getLastName())
                .build();

        finishRegistrationRequestDto = DtoBuilder.getFinishDto();
    }

    @Test
    public void testCreateClient() {
        // Given
        Client clientToSave = DealMapper.initializeClient(loanStatementRequestDto);

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        // When
        Client result = clientService.createClient(loanStatementRequestDto);

        // Then
        assertEquals(savedClient.getFirstName(), result.getFirstName());
        assertEquals(savedClient.getLastName(), result.getLastName());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    public void testFinalUpdateClient() {
        // Given
        // When
        Client updatedClient = clientService.finalUpdateClient(savedClient, finishRegistrationRequestDto);
        // Then
        assertEquals(finishRegistrationRequestDto.getGender(), updatedClient.getGender());
        assertEquals(finishRegistrationRequestDto.getMaritalStatus(), updatedClient.getMaritalStatus());
        assertEquals(finishRegistrationRequestDto.getDependentAmount(), updatedClient.getDependentAmount());
        assertEquals(finishRegistrationRequestDto.getAccountNumber(), updatedClient.getAccountNumber());
    }
}