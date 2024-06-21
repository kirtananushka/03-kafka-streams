package com.tananushka.mom.kafka.streams.task4.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tananushka.mom.kafka.streams.task4.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class EmployeeDeserializer implements Deserializer<Employee> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public Employee deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, Employee.class);
        } catch (Exception e) {
            log.error("Error deserializing employee");
            throw new SerializationException("Error deserializing employee", e);
        }
    }

    @Override
    public void close() {
    }
}