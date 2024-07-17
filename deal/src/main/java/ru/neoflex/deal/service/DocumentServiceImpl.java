package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.dto.EmailMessage;
import ru.neoflex.deal.dto.StatementStatusHistoryDto;
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.deal.enums.ApplicationStatus;
import ru.neoflex.deal.enums.ChangeType;
import ru.neoflex.deal.enums.CreditStatus;
import ru.neoflex.deal.enums.TopicType;
import ru.neoflex.deal.exception.SesCodeIsNotValidException;
import ru.neoflex.deal.exception.StatementStatusIsNotValidException;
import ru.neoflex.deal.kafka.KafkaProducer;
import ru.neoflex.deal.service.interfaces.DocumentService;
import ru.neoflex.deal.service.interfaces.StatementService;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final KafkaProducer kafkaProducer;
    private final StatementService statementService;

    @Override
    public void finishRegistration(UUID statementId, String email) {
        log.info("Sending FINISH_REGISTRATION message");
        kafkaProducer.sendEmail(EmailMessage.builder()
                .statementId(statementId)
                .theme(TopicType.FINISH_REGISTRATION)
                .address(email)
                .build());
    }

    @Override
    public void createDocument(UUID statementId, String email) {
        log.info("Sending CREATE_DOCUMENTS message");
        kafkaProducer.sendEmail(EmailMessage.builder()
                .statementId(statementId)
                .theme(TopicType.CREATE_DOCUMENTS)
                .address(email)
                .build());
    }

    @Override
    public void sendDocument(UUID statementId) {
        Statement statement = statementService.getStatementById(statementId);
        statement.setApplicationStatus(ApplicationStatus.PREPARE_DOCUMENTS);
        statement.getStatusHistory().add(StatementStatusHistoryDto.builder()
                .status(ApplicationStatus.PREPARE_DOCUMENTS)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build());
        statementService.updateStatement(statement);

        log.info("Sending SEND_DOCUMENTS message");

        kafkaProducer.sendEmail(EmailMessage.builder()
                .statementId(statementId)
                .theme(TopicType.SEND_DOCUMENTS)
                .address(statement.getClient().getEmail())
                .build());

    }

    @Override
    public void signDocument(UUID statementId, boolean isSigned) {
        Statement statement = statementService.getStatementById(statementId);
        if (!statement.getApplicationStatus().equals(ApplicationStatus.PREPARE_DOCUMENTS)) {
            throw new StatementStatusIsNotValidException("Статус вашего заявления не позволяет Вам выполнить это действие");
        }
        if (!isSigned) {
            statement.setApplicationStatus(ApplicationStatus.CLIENT_DENIED);
            statement.getStatusHistory().add(StatementStatusHistoryDto.builder()
                    .status(ApplicationStatus.CLIENT_DENIED)
                    .time(LocalDateTime.now())
                    .changeType(ChangeType.AUTOMATIC)
                    .build());
            statementService.updateStatement(statement);
            log.info("CLIENT_DENIED, statement id={} is closed", statementId.toString());
        } else {
            statement.setSesCode("123");
            statement.setApplicationStatus(ApplicationStatus.DOCUMENT_SIGNED);
            statement.getStatusHistory().add(StatementStatusHistoryDto.builder()
                    .status(ApplicationStatus.DOCUMENT_SIGNED)
                    .time(LocalDateTime.now())
                    .changeType(ChangeType.AUTOMATIC)
                    .build());
            statement.setSignDate(LocalDateTime.now());
            statementService.updateStatement(statement);
            kafkaProducer.sendEmail(EmailMessage.builder()
                    .statementId(statementId)
                    .theme(TopicType.SEND_SES)
                    .address(statement.getClient().getEmail())
                    .build());
            log.info("Sending SEND_SES message");
        }
    }

    @Override
    public void verifyCode(UUID statementId, String code) {
        Statement statement = statementService.getStatementById(statementId);
        if (!statement.getApplicationStatus().equals(ApplicationStatus.DOCUMENT_SIGNED)) {
            throw new StatementStatusIsNotValidException("Статус вашего заявления не позволяет Вам выполнить это действие");
        }
        if (statement.getSesCode().equals(code)) {
            statement.setApplicationStatus(ApplicationStatus.CREDIT_ISSUED);
            statement.getStatusHistory().add(StatementStatusHistoryDto.builder()
                    .status(ApplicationStatus.CREDIT_ISSUED)
                    .time(LocalDateTime.now())
                    .changeType(ChangeType.AUTOMATIC)
                    .build());
            statement.getCredit().setCreditStatus(CreditStatus.ISSUED);
            statementService.updateStatement(statement);

            kafkaProducer.sendEmail(EmailMessage.builder()
                    .statementId(statementId)
                    .theme(TopicType.CREDIT_ISSUED)
                    .address(statement.getClient().getEmail())
                    .build());
            log.info("Sending CREDIT_ISSUED message");
        } else {
            throw new SesCodeIsNotValidException("code is not valid");
        }
    }

    @Override
    public void sendDenied(UUID statementId, String address) {
        Statement statement = statementService.getStatementById(statementId);
        kafkaProducer.sendEmail(EmailMessage.builder()
                .statementId(statementId)
                .theme(TopicType.STATEMENT_DENIED)
                .address(statement.getClient().getEmail())
                .build());
        log.info("Sending STATEMENT_DENIED message. Statement id={} is closed", statementId.toString());
    }
}
