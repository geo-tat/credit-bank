package ru.neoflex.deal.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.neoflex.deal.dto.EmailMessage;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "kafka.enabled", matchIfMissing = false)
public class KafkaProducerConfiguration {

    @Value("${kafka.bootstrapAddress}")
    private String SERVER;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
        return new KafkaAdmin(configs);
    }

    @Bean
    public ProducerFactory<String, EmailMessage> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, EmailMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic finishRegistration(){
        return new NewTopic("finish-registration",1,(short) 1);
    }
    @Bean
    public NewTopic createDocuments(){
        return new NewTopic("create-documents",1,(short) 1);
    }
    @Bean
    public NewTopic sendDocuments(){
        return new NewTopic("send-documents",1,(short) 1);
    }
    @Bean
    public NewTopic sendSes(){
        return new NewTopic("send-ses",1,(short) 1);
    }
    @Bean
    public NewTopic creditIssued(){
        return new NewTopic("credit-issued",1,(short) 1);
    }
    @Bean
    public NewTopic statementDenied(){
        return new NewTopic("statement-denied",1,(short) 1);
    }
}
