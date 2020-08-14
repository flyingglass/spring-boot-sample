package com.example.kafka;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * @author ly
 * @date 2020/8/13 16:18
 */
@Slf4j
public class KafkaRequest extends KafkaMessage {
    private static final long serialVersionUID = -2437059090445485024L;

    public KafkaRequest() {
        String messageId = UUID.randomUUID().toString();
        super.setMessageId(messageId);
    }

    /**
     * Request的messageId自动生成
     * @param messageId
     */
    @Override
    public void setMessageId(String messageId) {

    }
}
