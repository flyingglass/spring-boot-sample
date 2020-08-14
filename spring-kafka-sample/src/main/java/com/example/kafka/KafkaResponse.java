package com.example.kafka;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ly
 * @date 2020/8/13 16:17
 */
@Slf4j
@Getter
@Setter
public class KafkaResponse extends KafkaMessage {
    private static final long serialVersionUID = 6835385628635487093L;

    private Object result;
}
