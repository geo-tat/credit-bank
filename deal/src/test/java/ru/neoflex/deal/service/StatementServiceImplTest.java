package ru.neoflex.deal.service;

import jakarta.persistence.EntityNotFoundException;
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
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.deal.mapper.DealMapper;
import ru.neoflex.deal.repository.StatementRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class StatementServiceImplTest {

    @Mock
    StatementRepository statementRepository;

    @InjectMocks
    StatementServiceImpl statementService;

    Client client;

    LoanStatementRequestDto loanStatementRequestDto;

    FinishRegistrationRequestDto finishRegistrationRequestDto;

    @BeforeEach
    public void setUp() {
        loanStatementRequestDto = DtoBuilder.getLoanStatementRequestDto();
        finishRegistrationRequestDto = DtoBuilder.getFinishDto();
    }

    @Test
    void testCreateStatement() {
        // Given
        client = DtoBuilder.getClient(finishRegistrationRequestDto, loanStatementRequestDto);

        LocalDateTime registrationTime = LocalDateTime.now();

        Statement statementToSave = DealMapper.initializeStatement(registrationTime, client);
        UUID statementId = UUID.randomUUID();
        statementToSave.setId(statementId);

        Statement savedStatement = Statement.builder().build();
        savedStatement.setId(statementId);
        savedStatement.setClient(client);
        savedStatement.setCreationDate(registrationTime);

        // When
        when(statementRepository.save(any(Statement.class))).thenReturn(savedStatement);
        Statement result = statementService.createStatement(client, loanStatementRequestDto);

        // Then
        assertEquals(savedStatement.getId(), result.getId());
        assertEquals(savedStatement.getClient(), result.getClient());
        assertEquals(savedStatement.getCreationDate(), result.getCreationDate());

        verify(statementRepository).save(any(Statement.class));
    }

    @Test
    void testGetStatementById() {
        // Given
        UUID statementId = UUID.randomUUID();
        Statement statement = new Statement();
        statement.setId(statementId);

        // When
        when(statementRepository.findById(statementId)).thenReturn(Optional.of(statement));
        Statement result = statementService.getStatementById(statementId);

        // Then
        assertEquals(statementId, result.getId());

        verify(statementRepository).findById(statementId);
    }

    @Test
    void testGetStatementByIdNotFound() {
        // Given
        UUID statementId = UUID.randomUUID();

        // When & Then
        when(statementRepository.findById(statementId)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> statementService.getStatementById(statementId));

        assertEquals("Заявление не найдено по ID=" + statementId, exception.getMessage());

        verify(statementRepository).findById(statementId);
    }

    @Test
    void testUpdateStatement() {
        // Given
        UUID statementId = UUID.randomUUID();
        Statement statement = new Statement();
        statement.setId(statementId);

        // When
        when(statementRepository.save(any(Statement.class))).thenReturn(statement);
        statementService.updateStatement(statement);

        // Then
        verify(statementRepository).save(statement);
    }

}