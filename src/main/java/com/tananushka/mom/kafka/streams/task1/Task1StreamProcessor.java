package com.tananushka.mom.kafka.streams.task1;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class Task1StreamProcessor {

    @Value("${kafka.topic.task1-1}")
    private String inputTopic;

    @Value("${kafka.topic.task1-2}")
    private String outputTopic;

    @Bean
    public KStream<String, String> processTask1(StreamsBuilder kStreamBuilder) {
        log.info("Initializing Kafka Stream from {} to {}", inputTopic, outputTopic);
        KStream<String, String> stream = kStreamBuilder.stream(inputTopic);
        stream
                .peek((key, value) -> log.info("Received message from {} - Key: {}, Value: {}", inputTopic, key, value))
                .to(outputTopic);
        log.debug("Kafka Stream from {} to {} initialized successfully", inputTopic, outputTopic);
        return stream;
    }
}