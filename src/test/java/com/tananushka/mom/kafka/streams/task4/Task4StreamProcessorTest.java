package com.tananushka.mom.kafka.streams.task4;

import com.tananushka.mom.kafka.streams.config.TestConfig;
import com.tananushka.mom.kafka.streams.task4.entity.Employee;
import com.tananushka.mom.kafka.streams.task4.serde.EmployeeSerde;
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
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ContextConfiguration(classes = {Task4StreamProcessor.class, TestConfig.class})
public class Task4StreamProcessorTest {

    @Autowired
    private Task4StreamProcessor task4StreamProcessor;

    @Autowired
    private StreamsBuilder streamsBuilder;

    private TestInputTopic<String, Employee> inputTopic;
    private TestOutputTopic<String, Employee> outputTopic;

    @BeforeEach
    public void setUp() {
        task4StreamProcessor.processTask4(streamsBuilder);

        Properties props = new Properties();
        props.put("application.id", "test");
        props.put("bootstrap.servers", "dummy:1234");
        props.put("default.key.serde", Serdes.StringSerde.class.getName());
        props.put("default.value.serde", EmployeeSerde.class.getName());

        TopologyTestDriver testDriver = new TopologyTestDriver(streamsBuilder.build(), props);

        inputTopic = testDriver.createInputTopic(task4StreamProcessor.getInputTopic(), Serdes.String().serializer(), new EmployeeSerde().serializer());
        outputTopic = testDriver.createOutputTopic(task4StreamProcessor.getOutputTopic(), Serdes.String().deserializer(), new EmployeeSerde().deserializer());
    }

    @Test
    public void testTask4StreamProcessor() {
        Employee employee = new Employee();
        employee.setName("Giorgi");
        employee.setCompany("EPAM");
        employee.setPosition("developer");
        employee.setExperience(5);

        inputTopic.pipeInput(null, employee);

        TestRecord<String, Employee> record = outputTopic.readRecord();
        assertNotNull(record);
        assertNull(record.key());
        assertEquals("Giorgi", record.value().getName());
        assertEquals("EPAM", record.value().getCompany());
        assertEquals("developer", record.value().getPosition());
        assertEquals(5, record.value().getExperience());
    }
}
