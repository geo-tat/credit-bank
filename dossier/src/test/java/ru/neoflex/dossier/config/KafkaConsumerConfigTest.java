package ru.neoflex.dossier.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import ru.neoflex.dossier.dto.EmailMessage;
import ru.neoflex.dossier.properties.KafkaProperties;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class KafkaConsumerConfigTest {

    private KafkaProperties properties;

    @BeforeEach
    void setUp() {
        properties = new KafkaProperties();
        properties.setBootstrapAddress("localhost:9092");
        properties.setGroupId("testGroupId");
    }

    @Test
    void testConsumerFactory() {
        properties = new KafkaProperties();
        properties.setBootstrapAddress("localhost:9092");
        properties.setGroupId("testGroupId");

        KafkaConsumerConfig kafkaConsumerConfig = new KafkaConsumerConfig(properties);
        ConsumerFactory<String, EmailMessage> consumerFactory = kafkaConsumerConfig.consumerFactory();

        Map<String, Object> expectedConfigProps = new HashMap<>();
        expectedConfigProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        expectedConfigProps.put(ConsumerConfig.GROUP_ID_CONFIG, "testGroupId");
        expectedConfigProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);


        assertEquals(expectedConfigProps.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG), consumerFactory.getConfigurationProperties().get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals(expectedConfigProps.get(ConsumerConfig.GROUP_ID_CONFIG), consumerFactory.getConfigurationProperties().get(ConsumerConfig.GROUP_ID_CONFIG));
        assertEquals(expectedConfigProps.get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG), consumerFactory.getConfigurationProperties().get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
    }

    @Test
    void testKafkaListenerContainerFactory() {
        KafkaConsumerConfig kafkaConsumerConfig = new KafkaConsumerConfig(properties);
        ConcurrentKafkaListenerContainerFactory<String, EmailMessage> factory = kafkaConsumerConfig.kafkaListenerContainerFactory();

        assertNotNull(factory, "KafkaListenerContainerFactory should not be null");
    }
}
