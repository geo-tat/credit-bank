package ru.neoflex.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.neoflex.deal.entity.Client;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByEmail(String email);
}
