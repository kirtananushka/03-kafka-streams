package com.tananushka.mom.kafka.streams.task4;

import com.tananushka.mom.kafka.streams.task4.entity.Employee;
import com.tananushka.mom.kafka.streams.task4.serde.EmployeeSerde;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@Getter
public class Task4StreamProcessor {

    @Value("${kafka.topic.task4}")
    private String inputTopic;

    @Value("${kafka.topic.task4.output}")
    private String outputTopic;

    @Bean
    public KStream<String, Employee> processTask4(StreamsBuilder kStreamBuilder) {
        log.info("Initializing Kafka Stream from {}", inputTopic);

        KStream<String, Employee> stream = kStreamBuilder.stream(inputTopic, Consumed.with(Serdes.String(), new EmployeeSerde()));

        stream.filter((key, value) -> value != null)
                .peek((key, value) -> log.info("Received message from {} - Key: {}, Value: {}", inputTopic, key, value))
                .to(outputTopic);

        return stream;
    }
}
