package com.tananushka.mom.kafka.streams.config;

import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    @Bean
    public StreamsBuilder streamsBuilder() {
        return new StreamsBuilder();
    }
}
