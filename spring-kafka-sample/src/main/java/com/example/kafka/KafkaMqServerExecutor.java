package com.example.kafka;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ly
 * @date 2020/8/13 16:23
 */
@Slf4j
public class KafkaMqServerExecutor implements ServerExecutor{

    @Override
    public void start(String interfaze, ApplicationEntity applicationEntity) throws Exception {

    }

    @Override
    public boolean started(String interfaze, ApplicationEntity applicationEntity) throws Exception {
        return false;
    }
}
