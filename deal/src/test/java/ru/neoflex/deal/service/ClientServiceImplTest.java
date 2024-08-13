package ru.neoflex.deal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.neoflex.deal.DtoBuilder;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.dto.PassportDto;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.repository.ClientRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    void setUp() {
        loanStatementRequestDto = DtoBuilder.getLoanStatementRequestDto();

        savedClient = Client.builder()
                .firstName(loanStatementRequestDto.getFirstName())
                .lastName(loanStatementRequestDto.getLastName())
                .passportDtoData(new PassportDto())
                .build();

        finishRegistrationRequestDto = DtoBuilder.getFinishDto();
    }

    @Test
    void testCreateClient() {
        // Given
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        // When
        Client result = clientService.createClient(loanStatementRequestDto);

        // Then
        assertEquals(savedClient.getFirstName(), result.getFirstName());
        assertEquals(savedClient.getLastName(), result.getLastName());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void testCreateClientWhenExist() {
        // Given
        Client existingClient = DtoBuilder.getClient(finishRegistrationRequestDto,loanStatementRequestDto);
        when(clientRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.ofNullable(existingClient));
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);
        // When
        Client result = clientService.createClient(loanStatementRequestDto);

        // Then
        assertEquals(savedClient.getFirstName(), result.getFirstName());
        assertEquals(savedClient.getLastName(), result.getLastName());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void testFinalUpdateClient() {
        // Given
        // When
        Client updatedClient = clientService.finalUpdateClient(savedClient, finishRegistrationRequestDto);
        // Then
        assertEquals(finishRegistrationRequestDto.getGender(), updatedClient.getGender());
        assertEquals(finishRegistrationRequestDto.getMaritalStatus(), updatedClient.getMaritalStatus());
        assertEquals(finishRegistrationRequestDto.getDependentAmount(), updatedClient.getDependentAmount());
        assertEquals(finishRegistrationRequestDto.getAccountNumber(), updatedClient.getAccountNumber());
    }

    @Test
    void testGetAllClients() {
        // Given
        Client client1 = new Client();
        Client client2 = new Client();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Client> clientPage = new PageImpl<>(Arrays.asList(client1, client2), pageable, 2);

        // When
        when(clientRepository.findAll(pageable)).thenReturn(clientPage);
        Page<Client> result = clientService.getAllClients(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(clientRepository).findAll(pageable);
    }
}