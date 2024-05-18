package com.laowudi.controller;

import com.laowudi.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/kafka")
public class MessageController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/publish")
    public String publishMessage(@RequestBody Map map) {
        kafkaProducerService.sendMessage("test",map.get("data").toString());
        return "Message published successfully";
    }
}

