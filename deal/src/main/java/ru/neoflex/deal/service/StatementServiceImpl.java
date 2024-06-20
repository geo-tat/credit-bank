package ru.neoflex.deal.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.deal.mapper.DealMapper;
import ru.neoflex.deal.repository.StatementRepository;
import ru.neoflex.deal.service.interfaces.StatementService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatementServiceImpl implements StatementService {

    private final StatementRepository statementRepository;

    @Override
    public Statement createStatement(Client client, LoanStatementRequestDto dto) {
        LocalDateTime registrationTime = LocalDateTime.now();
        Statement statementToSave = DealMapper.InitializeStatement(registrationTime, client, dto);
        Statement statement = statementRepository.save(statementToSave);
        log.info("statement information saved in db {}", statement);
        return statement;
    }

    @Override
    public Statement getStatementById(UUID statementId) {
        Statement statement = statementRepository.findById(statementId)
                .orElseThrow(() -> new EntityNotFoundException("Заявление не найдено по ID=" + statementId));
        log.info("received statement from db {}", statement);
        return statement;
    }

    @Override
    public void updateStatement(Statement statement) {
        statementRepository.save(statement);
        log.info("updated statement saved in db");
    }
}
