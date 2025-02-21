package ru.neoflex.deal.service.interfaces;

import ru.neoflex.deal.entity.Credit;

import java.util.UUID;

public interface DocumentService {

    void finishRegistration(UUID statementId, String address);

    void createDocument(UUID statementId, String address, Credit credit);

    void sendDenied(UUID statementId, String address);

    void sendDocument(UUID statementId);

    void signDocument(UUID statementId, boolean isSigned);

    void verifyCode(UUID statementId, String code);

}
