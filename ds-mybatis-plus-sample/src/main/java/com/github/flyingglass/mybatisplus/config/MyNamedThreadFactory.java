package com.github.flyingglass.mybatisplus.config;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
// Copy from Alibaba Sentinel project
public class MyNamedThreadFactory implements ThreadFactory {
    private AtomicInteger count = new AtomicInteger(1);
    private ThreadGroup group;
    private String namePrefix;
    private boolean daemon;

    public MyNamedThreadFactory(String namePrefix) {
        this(namePrefix, false);
    }

    public MyNamedThreadFactory(String namePrefix, boolean daemon) {
        SecurityManager securityManager = System.getSecurityManager();
        this.group = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = namePrefix;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(group, runnable, namePrefix + "-thread-" + count.getAndIncrement(), 0);
        thread.setDaemon(daemon);

        return thread;
    }
}