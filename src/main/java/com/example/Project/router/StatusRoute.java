package com.example.Project.router;

import generated.ObjectFactory;
import generated.Player;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.UnmarshalException;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.springframework.stereotype.Component;

import java.io.StringReader;

@Component
public class StatusRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:status")
                .log("Send to status_topic : ${body}")
                //.to("kafka:status_topic?brokers={{kafka.broker1.host}}");
                .to("kafka:status_topic?brokers=localhost:9092");
    }
}