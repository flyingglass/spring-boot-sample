package com.example.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

/**
 * @author ly
 * @date 2020/8/14 10:29
 */
public class StringKafkaTemplate extends KafkaTemplate<String, String> {

    public StringKafkaTemplate(ProducerFactory<String, String> producerFactory) {
        super(producerFactory);
    }

    public StringKafkaTemplate(ProducerFactory<String, String> producerFactory, Map<String, Object> configOverrides) {
        super(producerFactory, configOverrides);
    }

    public StringKafkaTemplate(ProducerFactory<String, String> producerFactory, boolean autoFlush) {
        super(producerFactory, autoFlush);
    }

    public StringKafkaTemplate(ProducerFactory<String, String> producerFactory, boolean autoFlush, Map<String, Object> configOverrides) {
        super(producerFactory, autoFlush, configOverrides);
    }
}
