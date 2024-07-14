package ru.neoflex.dossier;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.neoflex.dossier.dto.EmailMessage;
import ru.neoflex.dossier.service.DossierService;

@Slf4j
@Service
@RequiredArgsConstructor
public class Consumer {

    private final DossierService service;

    @KafkaListener(topics = {
            "finish-registration",
            "credit-issued",
            "statement-denied"
    }, groupId = "group1")
    public void listen(EmailMessage message) {
        log.info("received message {}", message);

        switch (message.getTheme().getValue()) {
            case "finish-registration":
                service.send(message, "Ваша заявка предварительно одобрена, завершите оформление");
                break;
            case "credit-issued":
                service.send(message, "Кредит выдан!");
                break;
            case "statement-denied":
                service.send(message, "В кредите отказано!");
                break;
        }
    }

    @KafkaListener(topics = {
            "create-documents",
            "send-documents",
            "send-ses"
    }, groupId = "group1")
    public void processMessage(EmailMessage message) throws MessagingException {
        log.info("received message {}", message);

        switch (message.getTheme().getValue()) {
            case "create-documents":
                service.sendDocumentHtml(message);
                break;
            case "send-documents":
                service.signHtml(message);
                break;
            case "send-ses":
                service.verifyCodeHtml(message);
                break;
        }
    }
}
