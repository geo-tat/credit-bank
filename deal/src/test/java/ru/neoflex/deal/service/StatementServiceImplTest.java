package ru.neoflex.deal.service;

import jakarta.persistence.EntityNotFoundException;
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
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.deal.mapper.DealMapper;
import ru.neoflex.deal.repository.StatementRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


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
    void testFindAllStatements() {
        // Given
        Pageable pageable = PageRequest.of(4, 10);
        List<Statement> statementList = Arrays.asList(
                new Statement(),
                new Statement()
        );
        Page<Statement> statementPage = new PageImpl<>(statementList, pageable, statementList.size());
        // When
        when(statementRepository.findAll(pageable)).thenReturn(statementPage);
        Page<Statement> result = statementService.getAllStatements(pageable);
        // Then
        assertNotNull(result);
        assertEquals(statementList.size(), result.getNumberOfElements());
        assertEquals(statementList.size(), result.getContent().size());
        assertTrue(result.getContent().containsAll(statementList));
        assertEquals(4, result.getPageable().getPageNumber());
        assertEquals(10, result.getPageable().getPageSize());

        verify(statementRepository, times(1)).findAll(pageable);
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

    @Test
    void testGetAllStatementsByClientId() {
        // Given
        UUID clientId = UUID.randomUUID();
        Statement statement1 = new Statement();
        Statement statement2 = new Statement();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Statement> statementPage = new PageImpl<>(Arrays.asList(statement1, statement2), pageable, 2);

        when(statementRepository.findAllByClientId(clientId, pageable)).thenReturn(statementPage);

        // When
        Page<Statement> result = statementService.getAllStatementsByClientId(clientId, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().containsAll(List.of(statement1,statement2)));
        verify(statementRepository).findAllByClientId(clientId, pageable);
    }
}