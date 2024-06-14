package ru.neoflex.deal.service.interfaces;

import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.entity.Credit;

public interface CreditService {
    Credit createCredit(CreditDto creditDto);
}
