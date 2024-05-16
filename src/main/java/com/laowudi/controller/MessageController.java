package com.laowudi.controller;

import com.laowudi.service.KafkaProducerService1;
import com.laowudi.service.KafkaProducerService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka")
public class MessageController {

    @Autowired
    private KafkaProducerService1 kafkaProducerService1;

    @Autowired
    private KafkaProducerService2 kafkaProducerService2;

    @PostMapping("/publish1")
    public String publishMessage1(@RequestParam("message") String message) {
        kafkaProducerService1.sendMessage(message);
        return "Message published successfully by Producer 1";
    }

    @PostMapping("/publish2")
    public String publishMessage2(@RequestParam("message") String message) {
        kafkaProducerService2.sendMessage(message);
        return "Message published successfully by Producer 2";
    }
}
