package com.example.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @author ly
 * @date 2020/8/14 10:25
 */
@Slf4j
@SpringBootApplication
public class ServerApplication {

    @SuppressWarnings("AlibabaThreadPoolCreation")
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "server");

        ApplicationContext context = SpringApplication.run(ServerApplication.class, args);

        String topic = context.getEnvironment().getProperty("kafka.topic");

        StringKafkaTemplate kafkaTemplate = context.getBean(StringKafkaTemplate.class);

        final KafkaRequest request = new KafkaRequest();
        request.setInterfaze(ServerApplication.class.getCanonicalName());
        request.setAsync(true);
        request.setMethod("server main");

        log.info(request.toString());

        ThreadPoolExecutor executor = KafkaUtils.createThreadPoolExecutor(
                ServerApplication.class.getCanonicalName(),
                2*12, 2*12*2, 15*60*1000, false
        );

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        service.scheduleAtFixedRate(
                () -> executor.submit(() -> {
                    log.info("发送内容：{}", request.toString());
                    kafkaTemplate.send(new ProducerRecord<>(topic, request.getMessageId(), request.toString()))
                            .addCallback(
                                    (result) -> {
                                        log.info("发送成功======\n {}", result.toString());
                                    },
                                    (ex) -> {
                                        log.info("发送失败========\n 发送内容:{}\n 异常信息:{} ", request.toString(), ex.getMessage());
                                    }
                            );
                }),
                100L,
                (int) (Math.random() * 50),
                TimeUnit.MILLISECONDS
        );
    }

}
