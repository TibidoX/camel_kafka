package com.example.Project;

import generated.Player;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.processor.ExchangePatternProcessor;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(properties = {"kafka.broker1.camel-request-path=direct:requests"})
//@SpringBootTest(properties = {"kafka.broker1.host=localhost:9092", "kafka.broker2.host=localhost:59092", "kafka.broker1.camel-request-path=direct:requests"})
//@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpoints
public class Tests {

	@Autowired
	ProducerTemplate producerTemplate;
	@Autowired
	ConsumerTemplate consumerTemplate;

	@EndpointInject("mock:jpa:com.example.Project.entity.Player")
	public MockEndpoint saveToDb;

	@EndpointInject("mock:kafka:results")
	public MockEndpoint kafkaResults;

	@EndpointInject("mock:kafka:status_topic")
	public MockEndpoint kafkaStatusTopic;

	@Test
	public void saveToDBTest() throws InterruptedException {
		com.example.Project.entity.Player player = new com.example.Project.entity.Player();
		player.setAge(25);
		player.setName("Eric");
		//player.setWins(90);
		saveToDb.expectedBodiesReceived(player);

		String body = """
<?xml version="1.0" encoding="UTF-8"?>
    			<Player xmlns="/jaxb/gen">
    				<age>25</age>
    				<name>Eric</name>
    				<wins>90</wins>
    			</Player>
				""";
		producerTemplate.sendBody("direct:requests", body);

		MockEndpoint.assertIsSatisfied(saveToDb);
	}

	@Test
	public void kafkaResultsTest() throws InterruptedException {
		String body_xml = """
<?xml version="1.0" encoding="UTF-8"?>
    			<Player xmlns="/jaxb/gen">
    				<name>Masha</name>
    				<wins>1000</wins>
    				<age>109</age>
    			</Player>
				""";
		//String body_json = "{\"name\":\"Masha\",\"wins\":1000,\"age\":109}";
		String body_json = "{\"name\":\"Masha\",\"age\":109}";
		kafkaResults.expectedBodiesReceived(body_json);
		//kafkaResults.expectedMessageCount(1);
		producerTemplate.sendBody("direct:requests", body_xml);

		MockEndpoint.assertIsSatisfied(kafkaResults);
	}

	@Test
	public void sendOKStatusTest() throws InterruptedException {
		kafkaStatusTopic.expectedBodiesReceived("<status>ok</status><message>success</message>");
		String body = """
<?xml version="1.0" encoding="UTF-8"?>
    			<Player xmlns="/jaxb/gen">
    				<name>Kuku</name>
    				<wins>9</wins>
    				<age>9090</age>
    			</Player>
				""";
		producerTemplate.sendBody("direct:requests", body);

		kafkaStatusTopic.assertIsSatisfied(5000);
	}
	@Test
	public void sendErrorStatusTest() throws InterruptedException {
		kafkaStatusTopic.expectedBodiesReceived("<status>failed</status><message>Something went wrong while unmarshalling</message>");

		producerTemplate.sendBody("direct:requests", "Text");

		kafkaStatusTopic.assertIsSatisfied(5000);
	}
}