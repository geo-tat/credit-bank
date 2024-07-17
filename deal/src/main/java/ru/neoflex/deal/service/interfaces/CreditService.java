package ru.neoflex.deal.service.interfaces;

import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.entity.Credit;

import java.util.UUID;

public interface CreditService {
    Credit createCredit(CreditDto creditDto);
    Credit getCreditById(UUID id);
}
