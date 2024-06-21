package com.tananushka.mom.kafka.streams.task3;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class Task3StreamProcessor {

    @Value("${kafka.topic.task3-1}")
    private String topicOne;

    @Value("${kafka.topic.task3-2}")
    private String topicTwo;

    @Bean
    public KStream<Long, String> processTask3(StreamsBuilder kStreamBuilder) {
        log.info("Initializing Kafka Streams {}, {}", topicOne, topicTwo);

        KStream<String, String> inputOne = kStreamBuilder.stream(topicOne, Consumed.with(Serdes.String(), Serdes.String()));
        KStream<String, String> inputTwo = kStreamBuilder.stream(topicTwo, Consumed.with(Serdes.String(), Serdes.String()));

        KStream<Long, String> processedOne = processStream(inputOne);
        KStream<Long, String> processedTwo = processStream(inputTwo);

        KStream<Long, String> joinedStream = processedOne.join(
                processedTwo,
                (value1, value2) -> value1 + " | " + value2,
                JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(30)),
                StreamJoined.with(Serdes.Long(), Serdes.String(), Serdes.String())
        );

        joinedStream.peek((key, value) -> log.info("Joined message - Key: {}, Value: {}", key, value));

        return joinedStream;
    }

    private KStream<Long, String> processStream(KStream<String, String> stream) {
        return stream
                .filter((key, value) -> value != null && value.contains(":"))
                .map((key, value) -> {
                    String[] parts = value.split(":", 2);
                    Long newKey = Long.parseLong(parts[0]);
                    return KeyValue.pair(newKey, value);
                })
                .peek((key, value) -> log.info("Processed message - Key: {}, Value: {}", key, value));
    }
}
