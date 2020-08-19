package com.example.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Properties;

/**
 * @author ly
 * @date 2020/8/19 11:06
 */
@Slf4j
@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "server");

        ApplicationContext context = SpringApplication.run(ProducerApplication.class, args);

        Environment env = context.getEnvironment();
        String host = env.getProperty("kafka.host");
        String topic = env.getProperty("kafka.topic");

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 64*1024*1024);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16*1024);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 50);

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        KafkaRequest request = new KafkaRequest();
        producer.send(
                new ProducerRecord<>(topic, request.getMessageId(), request.toString()),
                (r, e)->{
                    if (e == null) {
                        log.info("发送成功======\n 发送内容:{}\n 发送结果:{}", request.toString(), r.toString());
                    } else {
                        log.info("发送失败========\n 发送内容:{}\n 异常信息:{} ", r.toString(), e.getMessage());
                    }
                }
        );



    }


}
