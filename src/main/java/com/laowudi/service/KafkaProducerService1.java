package com.laowudi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KafkaProducerService1 {

    private static final String TOPIC = "test";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate1;

    @Transactional("kafkaTransactionManager1")
    public void sendMessage(String message) {
        kafkaTemplate1.executeInTransaction(operations -> {
            operations.send(TOPIC, message);
            return true;
        });
    }
}
