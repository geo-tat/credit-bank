package ru.neoflex.deal.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.deal.entity.Statement;

import java.util.UUID;

public interface StatementRepository extends JpaRepository<Statement, UUID> {
    Page<Statement> findAll(Pageable pageable);
}
