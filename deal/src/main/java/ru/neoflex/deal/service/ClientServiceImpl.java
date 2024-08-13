package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.mapper.DealMapper;
import ru.neoflex.deal.repository.ClientRepository;
import ru.neoflex.deal.service.interfaces.ClientService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Override
    public Client createClient(LoanStatementRequestDto dto) {

        Client clientToSave = DealMapper.initializeClient(dto);
        Client client = clientRepository.save(clientToSave);
        log.info("client information saved in db {}", client);
        return client;

    }

    @Override
    public Client finalUpdateClient(Client client, FinishRegistrationRequestDto dto) {
        client.setGender(dto.getGender());
        client.setMaritalStatus(dto.getMaritalStatus());
        client.setEmploymentData(dto.getEmploymentDto());
        client.setDependentAmount(dto.getDependentAmount());
        client.setAccountNumber(dto.getAccountNumber());
        client.getPassportDtoData().setIssueDate(dto.getPassportIssueDate());
        client.getPassportDtoData().setIssueBranch(dto.getPassportIssueBranch());
        log.info("client information was updated {}", client);
        return client;
    }
}
