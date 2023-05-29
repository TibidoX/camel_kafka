package com.example.Project.router;

import generated.ObjectFactory;
import generated.Player;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.UnmarshalException;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class RequestRoute extends RouteBuilder {
    @Value("${kafka.broker1.camel-request-path}")
    private String from_path;

    @Override
    public void configure() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        JaxbDataFormat jaxb = new JaxbDataFormat(jaxbContext);
        //DataFormat jaxb = new JaxbDataFormat("generated");

//        onException(UnmarshalException.class)
//                .handled(true)
//                .setBody(simple("Something went wrong while unmarshalling"))
//                .to("direct:status")
//                .to("direct:metrics_router_increment_fail_messages")
//                .to("direct:metrics_router_stop_timer");

        // Kafka Consumer
        from(from_path)
                .to("direct:metrics_router_increment_total_messages")
                .to("direct:metrics_router_start_timer")
                .log("Message received from Kafka : ${body}")//
                .unmarshal(jaxb)
                .choice()
                .when(body().isInstanceOf(Player.class))
                .log("Message received from Kafka : ${body}")
                .log("    on the topic ${headers[kafka.TOPIC]}")
                .to("direct:save_to_db")
                .otherwise()
                .setBody(simple("XML data isn't instance of Weather"))
                .to("direct:status")
                .to("direct:metrics_router_increment_fail_messages")
                .to("direct:metrics_router_stop_timer");

    }
}