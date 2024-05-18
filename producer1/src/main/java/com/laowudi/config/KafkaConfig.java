package com.laowudi.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@EnableRetry
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(5); // 设置最大重试次数

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000); // 初始间隔时间
        backOffPolicy.setMultiplier(2.0); // 每次重试的间隔时间乘数
        backOffPolicy.setMaxInterval(10000); // 最大间隔时间

        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    @Bean
    public ProducerFactory<String, String> producerFactory() {

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


    // 发送消息到死信队列
    private void sendToDeadLetterQueue(ConsumerRecord<String, String> record) {
        KafkaTemplate<String, String> kafkaTemplate = kafkaTemplate();
        kafkaTemplate.send("dead-letter-topic", record.key(), record.value());
    }
}
