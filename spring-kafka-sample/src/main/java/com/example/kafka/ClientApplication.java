package com.example.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ly
 * @date 2020/8/14 10:25
 */
@Slf4j
@SpringBootApplication
public class ClientApplication {

    static final int CPUS = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "client");

        ApplicationContext context = SpringApplication.run(ClientApplication.class, args);

        Environment env = context.getEnvironment();

        String topic = env.getProperty("kafka.topic");
        String host = env.getProperty("kafka.host");

        Map<String, Object> map = new HashMap<>(4);
        map.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        map.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        map.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        map.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        ThreadPoolExecutor executor = KafkaUtils.createThreadPoolExecutor(
                ClientApplication.class.getCanonicalName(),
                CPUS * 2 * 4, CPUS * 2 * 8,
                15 * 60 * 1000, false
        );

        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                map.put(ConsumerConfig.GROUP_ID_CONFIG, topic + "-" + UUID.randomUUID().toString());
                Consumer<String, String> consumer = new KafkaConsumer<String, String>(map);
                int partitionIdx = getPartitionIndex(consumer, topic, host);
                TopicPartition partition = new TopicPartition(topic, partitionIdx);
                consumer.assign(Arrays.asList(partition));

                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100L));
//                    log.info("开始消费");
                    if (null != records && records.count() != 0) {
                        for (ConsumerRecord<String, String> record: records){
                            log.info(record.value());
                            // 扔给客户端处理（Spring-Kafka采用KafkaListener Aop配合处理）
                        }
                    }
                }

            });
        }
    }

    private static int getPartitionIndex(Consumer<String, String> consumer, String topic, String key) {
        int partitionNumber = consumer.partitionsFor(topic).size();

        StringSerializer keySerializer = new StringSerializer();
        byte[] serializedKey = keySerializer.serialize(topic, key);

        int positive = Utils.murmur2(serializedKey) & 0x7fffffff;

        return positive % partitionNumber;
    }

}
