package com.example.Project.router;

import com.example.Project.*;
import com.example.Project.dto.PlayerDTO;
import com.example.Project.entity.Player;
import com.example.Project.mapper.PlayerMapper;
import lombok.RequiredArgsConstructor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerRoute extends RouteBuilder {

    private final PlayerMapper mapper;

    @Override
    public void configure() {
        onException(Exception.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "Something went wrong")
                .setHeader("MessageType", simple("ERROR"))
                .setBody(exceptionMessage())
                .to("direct:status")
                .markRollbackOnly();

        from("direct:player")
                .routeId("Player processing")
                .transacted()
                .to("direct:save_to_db")
                .to("direct:save_to_kafka")
                .setHeader("MessageType", simple("SUCCESS"))
                .to("direct:status");

        from("direct:save_to_db")
                .routeId("Save to database")
                .process(exchange -> {
                    generated.Player in = exchange.getIn().getBody(generated.Player.class);
                    Player player = mapper.mapGenerated(in);

                    exchange.getMessage().setBody(player, Player.class);
                })
                .log("Saving ${body} to database...")
                .to("jpa:om.example.Project.entity.Player");

        from("direct:save_to_kafka")
                .routeId("Save to kafka")
                .process(exchange -> {
                    Player player = exchange.getIn().getBody(Player.class);
                    PlayerDTO dto = mapper.mapWithoutId(player);

                    exchange.getMessage().setBody(dto, PlayerDTO.class);
                })
                .log("Saving ${body} to kafka")
                .marshal().json(JsonLibrary.Jackson)
                .setHeader(KafkaConstants.KEY, simple("camel"))
                .to("kafka:results?brokers={{kafka.broker2.host}}");
    }
}
