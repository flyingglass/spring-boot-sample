package com.example.kafka;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;

import java.io.Serializable;

/**
 * @author ly
 * @date 2020/8/13 16:13
 */
@Slf4j
@Getter
@Setter
@ToString
public class KafkaMessage implements Serializable {
    private static final long serialVersionUID = 7574060189430611713L;

    protected String messageId;
    protected String traceId;
    protected String interfaze;
    protected String method;
    protected Class<?>[] parameterTypes;
    protected Object[] parameters;
    protected boolean async;

}
