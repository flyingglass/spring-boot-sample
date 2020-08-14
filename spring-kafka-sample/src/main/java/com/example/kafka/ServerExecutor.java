package com.example.kafka;

/**
 * @author ly
 * @date 2020/8/13 16:23
 */
public interface ServerExecutor {

    /**
     * 启动
     * @param interfaze
     * @param applicationEntity
     * @throws Exception
     */
    void start(String interfaze, ApplicationEntity applicationEntity) throws Exception;

    /**
     * 已启动
     * @param interfaze
     * @param applicationEntity
     * @return
     * @throws Exception
     */
    boolean started(String interfaze, ApplicationEntity applicationEntity) throws Exception;
}
