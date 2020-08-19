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
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ly
 * @date 2020/8/14 10:25
 */
@Slf4j
@SpringBootApplication
public class ServerApplication {

    static final int CPUS = Runtime.getRuntime().availableProcessors();


    @SuppressWarnings("AlibabaThreadPoolCreation")
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "server");

        ApplicationContext context = SpringApplication.run(ServerApplication.class, args);


        Environment env = context.getEnvironment();
        String topic = env.getProperty("kafka.topic");
        String host = env.getProperty("kafka.host");


        ThreadPoolExecutor executor = KafkaUtils.createThreadPoolExecutor(
                ClientApplication.class.getCanonicalName(),
                CPUS * 2 * 4, CPUS * 2 * 8,
                15 * 60 * 1000, false
        );

        final KafkaRequest request = new KafkaRequest();
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 64*1024*1024);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16*1024);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 50);
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // 单Producer多线程
        for (int i = 0; i < 12; i++) {
            executor.execute(()-> {
                while (true) {
                    producer.send(
                            new ProducerRecord<>(topic, request.getMessageId(), request.toString()),
                            (r, e) -> {
                                if (e == null) {
//                                    log.info("发送成功======\n 发送内容:{}\n 发送结果:{}", request.toString(), r.toString());
                                } else {
                                    log.info("发送失败========\n 发送内容:{}\n 异常信息:{} ", r.toString(), e.getMessage());
                                }
                            }
                    );
                }
            });
        }

    }

}
