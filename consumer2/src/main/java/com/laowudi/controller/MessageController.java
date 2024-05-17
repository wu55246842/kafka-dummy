package com.laowudi.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
public class MessageController {


    @PostMapping("/health")
    public String publishMessage(@RequestParam("message") String message) {

        return "health";
    }
}

