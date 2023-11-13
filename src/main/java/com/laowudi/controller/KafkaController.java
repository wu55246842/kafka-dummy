package com.laowudi.controller;

import com.laowudi.query.MessageRequest;
import com.laowudi.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final KafkaProducerService producerService;

    @Autowired
    public KafkaController(KafkaProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/publish")
    public void sendMessageToKafkaTopic(@RequestBody MessageRequest request) {
        producerService.sendMessage("my-topic", request.getMessage());
    }
}

