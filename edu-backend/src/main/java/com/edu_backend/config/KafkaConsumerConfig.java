package com.edu_backend.config;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, String> consumerFactory() throws IOException {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "<aiven-kafka-broker-address>:9092");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "quiz-result-consumer-group");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put("security.protocol", "SSL");
        configProps.put("ssl.truststore.location", "C:\\Users\\rakes\\Desktop\\eduBackend\\edu-backend\\src\\main\\resources\\kafka-certificates\\ca.pem");
        configProps.put("ssl.keystore.location", "C:\\Users\\rakes\\Desktop\\eduBackend\\edu-backend\\src\\main\\resources\\kafka-certificates\\service.cert");
        configProps.put("ssl.key.location", "C:\\Users\\rakes\\Desktop\\eduBackend\\edu-backend\\src\\main\\resources\\kafka-certificates\\service.key");
        configProps.put("ssl.truststore.type", "PEM");
        configProps.put("ssl.keystore.type", "PEM");
        configProps.put("ssl.key.password", "<your-private-key-password-if-required>");
        return new DefaultKafkaConsumerFactory<>(configProps);
    }
}
