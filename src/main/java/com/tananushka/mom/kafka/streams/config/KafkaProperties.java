package com.tananushka.mom.kafka.streams.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaProperties {

    private Streams streams = new Streams();
    private String bootstrapServers;

    @Data
    public static class Streams {
        private String applicationId;
        private String defaultKeySerde;
        private String defaultValueSerde;
        private String defaultTimestampExtractor;
    }
}
