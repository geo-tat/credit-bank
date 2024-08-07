package ru.neoflex.dossier.service;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.neoflex.dossier.dto.EmailMessage;
import ru.neoflex.dossier.enums.TopicType;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)

public class ConsumerTest {

    @InjectMocks
    private Consumer consumer;

    @Mock
    private DossierService service;

    private EmailMessage message;

    @BeforeEach
    void setUp() {
        message = EmailMessage.builder()
                .statementId(UUID.randomUUID())
                .address("test@test.com")
                .build();
    }

    @Test
    void testListenFinishRegistration() {
        message.setTheme(TopicType.FINISH_REGISTRATION);

        consumer.listen(message);

        verify(service, times(1)).send(message, "Ваша заявка предварительно одобрена, завершите оформление");
    }

    @Test
    void testListenCreditIssued() {
        ;
        message.setTheme(TopicType.CREDIT_ISSUED);

        consumer.listen(message);

        verify(service, times(1)).send(message, "Кредит выдан!");
    }

    @Test
    void testListenStatementDenied() {
        message.setTheme(TopicType.STATEMENT_DENIED);

        consumer.listen(message);

        verify(service, times(1)).send(message, "В кредите отказано!");
    }

    @Test
    void testProcessMessageCreateDocuments() throws MessagingException {
        message.setTheme(TopicType.CREATE_DOCUMENTS);

        consumer.processMessage(message);

        verify(service, times(1)).sendDocumentHtml(message);
    }

    @Test
    void testProcessMessageSendDocuments() throws MessagingException {
        message.setTheme(TopicType.SEND_DOCUMENTS);

        consumer.processMessage(message);

        verify(service, times(1)).signHtml(message);
    }

    @Test
    void testProcessMessageSendSes() throws MessagingException {
        message.setTheme(TopicType.SEND_SES);

        consumer.processMessage(message);

        verify(service, times(1)).verifyCodeHtml(message);
    }
}
