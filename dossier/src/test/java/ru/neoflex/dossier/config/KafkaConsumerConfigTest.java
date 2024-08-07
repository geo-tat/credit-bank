package ru.neoflex.dossier.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import ru.neoflex.dossier.dto.EmailMessage;
import ru.neoflex.dossier.properties.KafkaProperties;

import java.util.HashMap;
import java.util.Map;

public class KafkaConsumerConfigTest {

    private KafkaProperties properties;

    @BeforeEach
    void setUp() {
        properties = new KafkaProperties();
        properties.setBootstrapAddress("localhost:9092");
        properties.setGroupId("testGroupId");
    }

    @Test
    public void testConsumerFactory() {
        properties = new KafkaProperties();
        properties.setBootstrapAddress("localhost:9092");
        properties.setGroupId("testGroupId");

        KafkaConsumerConfig kafkaConsumerConfig = new KafkaConsumerConfig(properties);
        ConsumerFactory<String, EmailMessage> consumerFactory = kafkaConsumerConfig.consumerFactory();

        Map<String, Object> expectedConfigProps = new HashMap<>();
        expectedConfigProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        expectedConfigProps.put(ConsumerConfig.GROUP_ID_CONFIG, "testGroupId");
        expectedConfigProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // Проверяем только ключевые параметры, которые должны быть установлены в ConsumerFactory
        Assertions.assertEquals(expectedConfigProps.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG), consumerFactory.getConfigurationProperties().get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
        Assertions.assertEquals(expectedConfigProps.get(ConsumerConfig.GROUP_ID_CONFIG), consumerFactory.getConfigurationProperties().get(ConsumerConfig.GROUP_ID_CONFIG));
        Assertions.assertEquals(expectedConfigProps.get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG), consumerFactory.getConfigurationProperties().get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
    }

    @Test
    public void testKafkaListenerContainerFactory() {
        KafkaConsumerConfig kafkaConsumerConfig = new KafkaConsumerConfig(properties);
        ConcurrentKafkaListenerContainerFactory<String, EmailMessage> factory = kafkaConsumerConfig.kafkaListenerContainerFactory();

        // Add assertions based on your actual setup
        // For example, you can check if the consumer factory set in the listener container factory is the one you expect
    }
}
