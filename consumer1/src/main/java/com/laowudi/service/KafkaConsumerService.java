package com.laowudi.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.laowudi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class KafkaConsumerService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @KafkaListener(topics = "test",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message, Acknowledgment acknowledgment) {
        try {
            Map<String, Object> map = objectMapper.readValue(message, Map.class);
            User user = new User();

            user.setSeq(Long.parseLong(map.get("seq").toString()));
            user.setUsername(map.get("username").toString());
            user.setPassword(map.get("password").toString());
            user.setCreateBy(map.get("createBy").toString());
            user.setUpdateBy("consumer_1");

            // Parse the produceTime
            String produceTimeString = map.get("produceTime").toString();
            OffsetDateTime produceTime = OffsetDateTime.parse(produceTimeString);
            user.setProduceTime(produceTime);

            userService.save(user);
            System.out.println("Consumer 1 consumed message: " + user);

            // 手动提交偏移量
            acknowledgment.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error processing message: " + message);
            throw e; // 让SeekToCurrentErrorHandler处理异常
        }
    }
}


