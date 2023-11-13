package com.laowudi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laowudi.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TestService testService;

    @KafkaListener(topics = "my-topic", groupId = "my-consumer-group")
    public void consume(String message) throws JsonProcessingException {
        //System.out.println("Consumed message: " + message);
        Person p = objectMapper.readValue(message, Person.class);
        testService.doIt(p);
    }
}

