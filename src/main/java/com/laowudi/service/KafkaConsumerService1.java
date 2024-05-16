package com.laowudi.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService1 {

    @KafkaListener(topics = "test", groupId = "my-group-1")
    public void consume(String message) {
        System.out.println("Consumer 1 consumed message: " + message);
        // 这里加入幂等处理逻辑
    }
}
