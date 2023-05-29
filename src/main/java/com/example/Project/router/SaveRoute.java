package com.example.Project.router;

import com.example.Project.dto.PlayerDTO;
import generated.Player;
import com.example.Project.mapper.PlayerMapper;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.catalog.JSonSchemaResolver;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;
import org.apache.camel.model.dataformat.JaxbDataFormat;

@Component
@RequiredArgsConstructor
public class SaveRoute extends RouteBuilder {
    private final PlayerMapper mapper;

    public void configure() {
        from("direct:save_to_db")
                .choice()
                .when(body().isInstanceOf(Player.class))
                .log("Message received from Kafka : ${body}")
                .log("    on the topic ${headers[kafka.TOPIC]}")
                .process(exchange -> {
                    Player in = exchange.getIn().getBody(Player.class);
                    com.example.Project.entity.Player player = mapper.mapGenerated(in);

                    exchange.getMessage().setBody(player, com.example.Project.entity.Player.class);
                })
                .log("Saving ${body} to database...")
                .to("jpa:com.example.Project.entity.Player")
                .process(exchange -> {
                    com.example.Project.entity.Player in = exchange.getIn().getBody(com.example.Project.entity.Player.class);
                    PlayerDTO player = mapper.mapWithoutId(in);

                    exchange.getMessage().setBody(player, PlayerDTO.class);
                })
                .marshal().json(JsonLibrary.Jackson)
                //.marshal().fhirXml()
                .log("Saving ${body} to kafka")
                .to("kafka:results?brokers=localhost:9092")
                .setBody(simple("<status>ok</status>"))
                .to("direct:status")
                .to("direct:metrics_router_increment_success_messages")
                .to("direct:metrics_router_stop_timer")
                .otherwise()
                .setBody(simple("<status>error</status><message>XML data isn't instance of Weather</message>"))
                .to("direct:status")
                .to("direct:metrics_router_increment_fail_messages")
                .to("direct:metrics_router_stop_timer");
    }
}