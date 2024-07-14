package ru.neoflex.deal.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.entity.Credit;
import ru.neoflex.deal.mapper.DealMapper;
import ru.neoflex.deal.repository.CreditRepository;
import ru.neoflex.deal.service.interfaces.CreditService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;

    @Override
    public Credit createCredit(CreditDto creditDto) {

        Credit creditToSave = DealMapper.initializeCredit(creditDto);

        Credit credit = creditRepository.save(creditToSave);
        log.info("credit information saved in db {}", credit);

        return credit;
    }

    @Override
    public Credit getCreditById(UUID id) {
        return creditRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Кредит не найден по ID=" + id));
    }
}
