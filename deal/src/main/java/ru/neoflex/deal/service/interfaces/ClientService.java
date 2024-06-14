package ru.neoflex.deal.service.interfaces;

import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.entity.Client;

public interface ClientService {
    Client createClient(LoanStatementRequestDto dto);

    Client finalUpdateClient(Client client, FinishRegistrationRequestDto dto);
}
