package com.tananushka.mom.kafka.streams.task2;

import com.tananushka.mom.kafka.streams.config.TestConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.TestRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ContextConfiguration(classes = {Task2StreamProcessor.class, TestConfig.class})
public class Task2StreamProcessorTest {

    @Autowired
    private Task2StreamProcessor task2StreamProcessor;

    @Autowired
    private StreamsBuilder streamsBuilder;

    private TestInputTopic<String, String> inputTopic;
    private TestOutputTopic<Integer, String> outputTopic;

    @BeforeEach
    public void setUp() {
        task2StreamProcessor.processTask2(streamsBuilder);

        Properties props = new Properties();
        props.put("application.id", "test");
        props.put("bootstrap.servers", "dummy:1234");
        props.put("default.key.serde", Serdes.StringSerde.class.getName());
        props.put("default.value.serde", Serdes.StringSerde.class.getName());

        TopologyTestDriver testDriver = new TopologyTestDriver(streamsBuilder.build(), props);

        inputTopic = testDriver.createInputTopic(task2StreamProcessor.getInputTopic(), Serdes.String().serializer(), Serdes.String().serializer());
        outputTopic = testDriver.createOutputTopic(task2StreamProcessor.getOutputTopic(), Serdes.Integer().deserializer(), Serdes.String().deserializer());
    }

    @Test
    public void testTask2StreamProcessor() {
        inputTopic.pipeInput(null, "Hello world, this is an example sentence.");

        TestRecord<Integer, String> record1 = outputTopic.readRecord();
        assertNotNull(record1);
        assertEquals(2, record1.key());
        assertEquals("an", record1.value());

        TestRecord<Integer, String> record2 = outputTopic.readRecord();
        assertNotNull(record2);
        assertEquals(7, record2.key());
        assertEquals("example", record2.value());
    }
}
