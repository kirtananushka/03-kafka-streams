package com.tananushka.mom.kafka.streams.task2;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
@Getter
public class Task2StreamProcessor {
    @Value("${kafka.topic.task2}")
    private String inputTopic;

    @Value("${kafka.topic.task2.output}")
    private String outputTopic;

    @Bean
    public KStream<Integer, String> processTask2(StreamsBuilder kStreamBuilder) {
        log.info("Initializing Kafka Stream from {}", inputTopic);
        KStream<String, String> stream = kStreamBuilder.stream(inputTopic);
        KStream<String, String> filteredStream = stream.filter((key, value) -> value != null);
        filteredStream.peek((key, value) -> log.info("Received message from {} - Key: {}, Value: {}", inputTopic, key, value));
        return filteredStream.flatMapValues(this::splitSentenceIntoWords)
                .map((key, value) -> new KeyValue<>(value.length(), value))
                .peek((key, value) -> log.info("Processed word - Key: {}, Word: {}", key, value));
    }

    @Bean
    public Map<String, KStream<Integer, String>> splitStreams(@Qualifier("processTask2") KStream<Integer, String> processedStream) {
        return processedStream.split(Named.as("words-"))
                .branch((key, value) -> key < 10, Branched.as("short"))
                .branch((key, value) -> key >= 10, Branched.as("long"))
                .noDefaultBranch();
    }

    @Bean
    public KStream<Integer, String> filterShortWordsWithA(@Qualifier("processTask2") KStream<Integer, String> processedStream) {
        Map<String, KStream<Integer, String>> branches = splitStreams(processedStream);
        return branches.get("words-short").filter((key, value) -> value.contains("a"))
                .peek((key, value) -> log.info("Filtered short word with 'a' - Key: {}, Value: {}", key, value));
    }

    @Bean
    public KStream<Integer, String> filterLongWordsWithA(@Qualifier("processTask2") KStream<Integer, String> processedStream) {
        Map<String, KStream<Integer, String>> branches = splitStreams(processedStream);
        return branches.get("words-long").filter((key, value) -> value.contains("a"))
                .peek((key, value) -> log.info("Filtered long word with 'a' - Key: {}, Value: {}", key, value));
    }

    @Bean
    public KStream<Integer, String> mergeAndLogFilteredStreams(StreamsBuilder kStreamBuilder) {
        KStream<Integer, String> processedStream = processTask2(kStreamBuilder);
        KStream<Integer, String> shortWordsWithA = filterShortWordsWithA(processedStream);
        KStream<Integer, String> longWordsWithA = filterLongWordsWithA(processedStream);
        KStream<Integer, String> mergedStream = shortWordsWithA.merge(longWordsWithA);
        mergedStream.peek((key, value) -> log.info("Merged and filtered stream - Key: {}, Value: {}", key, value));
        mergedStream.to(outputTopic, Produced.with(Serdes.Integer(), Serdes.String()));
        return mergedStream;
    }

    private List<String> splitSentenceIntoWords(String sentence) {
        return Arrays.asList(sentence.split("\\s+"));
    }
}
