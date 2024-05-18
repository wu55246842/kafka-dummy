package com.laowudi.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);


        // Acknowledgment strategy
        // 0: The producer sends the message and doesn't wait for any acknowledgment. This is efficient but can result in data loss and has no retry mechanism.
        // 1: The message is acknowledged after it is sent to the Leader and written to disk. If the Leader fails and the Followers have not yet synchronized the data, the data may be lost.
        // -1: The message is acknowledged only after all replicas have written it to disk. This ensures no data loss but may result in message duplication.
        configProps.put(ProducerConfig.ACKS_CONFIG, "-1");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3); // retry
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // Batch size for submitting data in bulk
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);// Buffer size for temporarily storing data on the producer side


        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
