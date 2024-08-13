package ru.neoflex.deal.service.interfaces;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.entity.Statement;

import java.util.Collection;
import java.util.UUID;

public interface StatementService {

    Statement createStatement(Client client, LoanStatementRequestDto dto);

    Statement getStatementById(UUID statementId);

    void updateStatement(Statement statement);

    Page<Statement> getAllStatements(Pageable pageable);

    Page<Statement> getAllStatementsByClientId(UUID clientId, Pageable pageable);
}
