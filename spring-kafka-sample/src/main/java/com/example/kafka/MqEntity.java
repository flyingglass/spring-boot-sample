package com.example.kafka;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author ly
 * @date 2020/8/13 15:59
 */
@Slf4j
public class MqEntity implements Serializable {

    private static final long serialVersionUID = -3603435458426745097L;

    private String interfaze;
    private String server;
    private String queueId;
    private String topicId;

}
