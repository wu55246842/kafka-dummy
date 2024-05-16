package com.laowudi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KafkaProducerService2 {

    private static final String TOPIC = "test";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate2;

    @Transactional("kafkaTransactionManager2")
    public void sendMessage(String message) {
        kafkaTemplate2.executeInTransaction(operations -> {
            operations.send(TOPIC, message);
            return true;
        });
    }
}
