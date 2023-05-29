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
public class MetricsRoute extends RouteBuilder {

    private long startTime = 0;
    private String messageBody;
    @Override
    public void configure() throws Exception {
        from("direct:metrics_router_increment_total_messages")
                .to("sql:UPDATE messages_count SET total = total + 1;");
        from("direct:metrics_router_increment_fail_messages")
                .to("sql:UPDATE messages_count SET fail = fail + 1;");
        from("direct:metrics_router_increment_success_messages")
                .to("sql:UPDATE messages_count SET success = success + 1;");
        from("direct:metrics_router_start_timer")
                .process(exchange -> {
                    startTime = System.currentTimeMillis();
                    messageBody = exchange.getIn().getBody(String.class);
                });
        from("direct:metrics_router_stop_timer")
                .process(exchange -> {
                    exchange.setProperty("message", messageBody);
                    exchange.setProperty("time", System.currentTimeMillis() - startTime);
                })
                .to("sql:INSERT INTO processing_time(message, milliseconds) VALUES(:#${exchangeProperty.message}, :#${exchangeProperty.time});");
    }
}