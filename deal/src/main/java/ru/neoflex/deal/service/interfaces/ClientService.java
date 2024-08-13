package ru.neoflex.deal.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.entity.Client;

public interface ClientService {
    Client createClient(LoanStatementRequestDto dto);

    Client finalUpdateClient(Client client, FinishRegistrationRequestDto dto);

    Page<Client> getAllClients(Pageable pageable);
}
