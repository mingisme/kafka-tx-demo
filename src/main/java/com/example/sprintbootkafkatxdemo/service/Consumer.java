package com.example.sprintbootkafkatxdemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Consumer {
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @KafkaListener(topics = "users", groupId = "group_id")
    public void consume(String message) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", message));
        if(message.startsWith("foo")){
            throw new RuntimeException("failed");
        }
    }

    @KafkaListener(groupId = "dltGroup", topics = "users.DLT")
    public void dltListen(String in) {
        logger.info("Received from DLT: " + in);
    }
}