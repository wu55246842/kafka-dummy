package com.laowudi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laowudi.model.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @KafkaListener(topics = "test",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord message, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
            String jsonString = message.value().toString().trim().replaceAll("^\"|\"$", "").replace("\\\"", "\"");
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonString);
            Map<String, Object> map = mapper.convertValue(jsonNode, Map.class);

            User user = new User();

            user.setSeq(Long.parseLong(map.get("seq").toString()));
            user.setUsername(map.get("username").toString());
            user.setPassword(map.get("password").toString());
            user.setCreateBy(map.get("createBy").toString());
            user.setUpdateBy("consumer_1");
            String produceTimeString = map.get("produceTime").toString();
            OffsetDateTime produceTime = OffsetDateTime.parse(produceTimeString);

            user.setProduceTime(produceTime);

            userService.save(user);
            System.out.println("Consumer 1 consumed message: " + user.toString());

            // Manually acknowledge the message
            acknowledgment.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error processing message: " + message.toString());
            throw e; // Let the DefaultErrorHandler handle the exception
        }
    }
}
