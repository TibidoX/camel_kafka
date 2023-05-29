package com.example.Project;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Sender {
    public static Sender instance;

    @Autowired
    ProducerTemplate producerTemplate;

    public void send()
    {
        producerTemplate.sendBody("kafka:requests?brokers=localhost:9092",  "test message #3");
    }

    @PostConstruct
    public void init()
    {
        instance = this;
    }
}