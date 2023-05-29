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

        onException(UnmarshalException.class)
                .handled(true)
                .setBody(simple("<status>failed</status><message>Something went wrong while unmarshalling</message>"))
                .to("direct:status")
                .to("direct:inc_fail")
                .to("direct:stop_timer");

        // Kafka Consumer
        from(from_path)
                .to("direct:inc_total")
                .to("direct:start_timer")
                .log("Message received from Kafka : ${body}")//
                .unmarshal(jaxb)
                .choice()
                .when(body().isInstanceOf(Player.class))
                .log("Message received from Kafka : ${body}")
                .log("    on the topic ${headers[kafka.TOPIC]}")
                .to("direct:save_to_db")
                .otherwise()
                .setBody(simple("<status>failed</status><message>XML data isn't instance of Player</message>"))
                .to("direct:status")
                .to("direct:inc_fail")
                .to("direct:stop_timer");

    }
}