package com.example.sprintbootkafkatxdemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class Consumer {
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public Consumer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

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

    @KafkaListener(id = "fooGroup2", topics = "topic2")
    public void listen(List<String> foos) throws IOException {
        logger.info("Received: " + foos);
        foos.forEach(f -> kafkaTemplate.send("topic3", f.toUpperCase()));
        logger.info("Messages sent, hit enter to commit tx");
        System.in.read();
    }

    @KafkaListener(id = "fooGroup3", topics = "topic3")
    public void listen(String in) {
        logger.info("Received: " + in);
    }
}
