package com.tananushka.mom.kafka.streams.task4.serde;

import com.tananushka.mom.kafka.streams.task4.entity.Employee;
import org.apache.kafka.common.serialization.Serdes;

public class EmployeeSerde extends Serdes.WrapperSerde<Employee> {
    public EmployeeSerde() {
        super(new EmployeeSerializer(), new EmployeeDeserializer());
    }
}
