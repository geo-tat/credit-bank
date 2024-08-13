package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.mapper.DealMapper;
import ru.neoflex.deal.repository.ClientRepository;
import ru.neoflex.deal.service.interfaces.ClientService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Override
    public Client createClient(LoanStatementRequestDto dto) {
        Optional<Client> existingClient = clientRepository.findByEmail(dto.getEmail());

        if (existingClient.isPresent()) {
            log.info("Клиент с адресом почты {} уже зарегистрирован", dto.getEmail());
            Client clientToSave = updateClientInformation(existingClient.get(), dto);
            Client client = clientRepository.save(clientToSave);
            log.info("client information saved in db {}", client);
            return client;
        }

        Client clientToSave = DealMapper.initializeClient(dto);
        Client client = clientRepository.save(clientToSave);
        log.info("client information saved in db {}", client);
        return client;

    }

    private Client updateClientInformation(Client client, LoanStatementRequestDto dto) {
        log.info("update client from LoanStatementRequestDto: clien: {}, dto {}", client, dto);
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        if (dto.getMiddleName() != null)
            client.setMiddleName(dto.getMiddleName());
        client.setBirthDate(dto.getBirthdate());
        client.getPassportDtoData().setSeries(dto.getPassportSeries());
        client.getPassportDtoData().setNumber(dto.getPassportNumber());

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

    @Override
    public Page<Client> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }
}
