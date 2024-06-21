package com.tananushka.mom.kafka.streams.task4.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tananushka.mom.kafka.streams.task4.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class EmployeeSerializer implements Serializer<Employee> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Employee data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            log.error("Error serializing employee");
            throw new SerializationException("Error serializing employee");
        }
    }

    @Override
    public void close() {
    }
}