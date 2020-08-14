package com.example.kafka;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author ly
 * @date 2020/8/13 16:24
 */
@Slf4j
@Getter
@Setter
public class ApplicationEntity implements Serializable {
    private static final long serialVersionUID = -8330164744383197236L;

    private String id;
    private String application;
    private String group;
    private String cluster;
    private String host;
    private int port;
    private long timestamp;

}
