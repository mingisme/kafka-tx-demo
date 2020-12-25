package com.example.sprintbootkafkatxdemo.controller;

import com.example.sprintbootkafkatxdemo.models.User;
import com.example.sprintbootkafkatxdemo.repository.UserRepository;
import com.example.sprintbootkafkatxdemo.service.Consumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/tx")
public class ProducerOnlyTxController {

    private final Logger logger = LoggerFactory.getLogger(ProducerOnlyTxController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Transactional("transactionManager")
    @PostMapping(path = "/add")
    public @ResponseBody
    String addNewUser1(@RequestParam String name
            , @RequestParam String email) throws JsonProcessingException {
        User n = new User();
        n.setName(name);
        n.setEmail(email);

        userRepository.save(n);
        logger.info("db save user {}", n);
        if(name.startsWith("0")){
            throw new RuntimeException("db fail");
        }

        ObjectMapper objectMapper =new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(n);
        logger.info("kafka produce {}", userJson);
        kafkaTemplate.send("user1",userJson);
        if(name.startsWith("1")){
            throw new RuntimeException("kafka fail");
        }
        kafkaTemplate.send("user1",userJson);
        if(name.startsWith("2")){
            throw new RuntimeException("kafka fail 2");
        }

        return "Saved";
    }
}
