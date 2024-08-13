package ru.neoflex.deal.kafka;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.neoflex.deal.dto.EmailMessage;
import ru.neoflex.deal.enums.TopicType;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, EmailMessage> kafkaTemplate;

    @InjectMocks
    private KafkaProducer kafkaProducer;

    @BeforeEach
    void setUp() {
        kafkaProducer = new KafkaProducer(kafkaTemplate);
    }

    @Test
    void sendEmail_shouldSendMessage() {
        // given
        EmailMessage message = EmailMessage.builder()
                .theme(TopicType.SEND_DOCUMENTS)
                .build();
        // when
        kafkaProducer.sendEmail(message);
        // then
        verify(kafkaTemplate, times(1)).send("send-documents", message);
    }
}