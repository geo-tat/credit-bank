package ru.neoflex.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.deal.entity.Credit;

import java.util.UUID;

public interface CreditRepository extends JpaRepository<Credit, UUID> {
}
