package ru.neoflex.deal.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.dto.EmailMessage;

@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;


    public void sendEmail(EmailMessage message) {
        log.info("message to transfer: {}", message);
        kafkaTemplate.send(message.getTheme().getValue(),message);
    }
}
