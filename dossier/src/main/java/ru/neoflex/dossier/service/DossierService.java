package ru.neoflex.dossier.service;

import jakarta.mail.MessagingException;
import ru.neoflex.dossier.dto.EmailMessage;

public interface DossierService {

    void send(EmailMessage emailMessage, String text);

    void signHtml(EmailMessage emailMessage) throws MessagingException;

    void sendDocumentHtml(EmailMessage emailMessage) throws MessagingException;

    void verifyCodeHtml(EmailMessage emailMessage) throws MessagingException;
}
