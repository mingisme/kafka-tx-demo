package com.example.sprintbootkafkatxdemo.controller;

import com.example.sprintbootkafkatxdemo.service.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {

    private final Producer producer;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    KafkaController(Producer producer, KafkaTemplate<String, String> kafkaTemplate) {
        this.producer = producer;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        this.producer.sendMessage(message);
    }

    @PostMapping(path = "/send/foos/{what}")
    public void sendFoo(@PathVariable String what) {
        this.kafkaTemplate.executeInTransaction(kafkaTemplate -> {
            StringUtils.commaDelimitedListToSet(what).stream()
                    .forEach(foo -> kafkaTemplate.send("topic2", foo));
            return null;
        });
    }
}