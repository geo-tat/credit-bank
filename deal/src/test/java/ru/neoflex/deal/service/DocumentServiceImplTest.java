package ru.neoflex.deal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.deal.dto.EmailMessage;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.entity.Credit;
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.deal.enums.ApplicationStatus;
import ru.neoflex.deal.exception.SesCodeIsNotValidException;
import ru.neoflex.deal.kafka.KafkaProducer;
import ru.neoflex.deal.service.interfaces.StatementService;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private StatementService statementService;

    @InjectMocks
    private DocumentServiceImpl documentService;

    private UUID statementId;
    private String email;

    @BeforeEach
    void setUp() {
        statementId = UUID.randomUUID();
        email = "test@example.com";
    }

    @Test
    void testFinishRegistration() {
        documentService.finishRegistration(statementId, email);

        verify(kafkaProducer, times(1)).sendEmail(any(EmailMessage.class));
    }

    @Test
    void testCreateDocument() {
        documentService.createDocument(statementId, email);

        verify(kafkaProducer, times(1)).sendEmail(any(EmailMessage.class));
    }

    @Test
    void testSendDocument() {
        Statement statement = new Statement();
        Client client = new Client();
        client.setEmail(email);
        statement.setClient(client);
        statement.setStatusHistory(new ArrayList<>());

        when(statementService.getStatementById(statementId)).thenReturn(statement);

        documentService.sendDocument(statementId);

        verify(statementService, times(1)).updateStatement(any(Statement.class));
        verify(kafkaProducer, times(1)).sendEmail(any(EmailMessage.class));
    }

    @Test
    void testSignDocumentWhenSigned() {
        Statement statement = new Statement();
        statement.setApplicationStatus(ApplicationStatus.PREPARE_DOCUMENTS);
        statement.setStatusHistory(new ArrayList<>());
        Client client = new Client();
        client.setEmail(email);
        statement.setClient(client);

        when(statementService.getStatementById(statementId)).thenReturn(statement);

        documentService.signDocument(statementId, true);

        verify(statementService, times(1)).updateStatement(any(Statement.class));
        verify(kafkaProducer, times(1)).sendEmail(any(EmailMessage.class));
    }

    @Test
    void testSignDocumentWhenNotSigned() {
        Statement statement = new Statement();
        statement.setApplicationStatus(ApplicationStatus.PREPARE_DOCUMENTS);
        statement.setStatusHistory(new ArrayList<>());

        when(statementService.getStatementById(statementId)).thenReturn(statement);

        documentService.signDocument(statementId, false);

        verify(statementService, times(1)).updateStatement(any(Statement.class));
    }

    @Test
    void testVerifyCodeWhenCodeIsValid() {
        Statement statement = new Statement();
        statement.setApplicationStatus(ApplicationStatus.DOCUMENT_SIGNED);
        statement.setSesCode("123");
        statement.setCredit(new Credit());
        statement.setStatusHistory(new ArrayList<>());
        Client client = new Client();
        client.setEmail(email);
        statement.setClient(client);

        when(statementService.getStatementById(statementId)).thenReturn(statement);

        documentService.verifyCode(statementId, "123");

        verify(statementService, times(1)).updateStatement(any(Statement.class));
        verify(kafkaProducer, times(1)).sendEmail(any(EmailMessage.class));
    }

    @Test
    void testVerifyCodeWhenCodeIsInvalid() {
        Statement statement = new Statement();
        statement.setApplicationStatus(ApplicationStatus.DOCUMENT_SIGNED);
        statement.setSesCode("123");

        when(statementService.getStatementById(statementId)).thenReturn(statement);

        assertThrows(SesCodeIsNotValidException.class, () -> documentService.verifyCode(statementId, "wrong_code"));
    }

    @Test
    void testSendDenied() {
        Statement statement = new Statement();
        Client client = new Client();
        client.setEmail(email);
        statement.setClient(client);

        when(statementService.getStatementById(statementId)).thenReturn(statement);

        documentService.sendDenied(statementId, email);

        verify(kafkaProducer, times(1)).sendEmail(any(EmailMessage.class));
    }
}
