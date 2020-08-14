package com.example.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ly
 * @date 2020/8/14 16:52
 */
public final class KafkaUtils {
    static final int CPUS = Runtime.getRuntime().availableProcessors();

    private static <K, V> int getPartitionIndex(Consumer<K, V> consumer, String topic, String key) {
        int partitionNumber = consumer.partitionsFor(topic).size();

        StringSerializer keySerializer = new StringSerializer();
        byte[] serializedKey = keySerializer.serialize(topic, key);

        int positive = Utils.murmur2(serializedKey) & 0x7fffffff;

        return positive % partitionNumber;
    }

    public static ThreadPoolExecutor createThreadPoolExecutor(
            String interfaze, int corePoolSize, int maximumPoolSize, long keepAliveTime, boolean allowCoreThreadTimeout
    ) {
        final String threadName = interfaze + "-" + "thread";
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024 * CPUS),
                new ThreadFactory() {
                    private AtomicInteger n = new AtomicInteger(0);
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, threadName + "-" + n.getAndIncrement());
                    }
                },
                (r, executor1) -> {
                    if (!executor1.isShutdown()) {
                        try {
                            executor1.getQueue().put(r);
                        } catch (InterruptedException e) {
                            // ignore
                        }
                    }
                }
        );
        executor.allowCoreThreadTimeOut(allowCoreThreadTimeout);
        return executor;
    }

    @SuppressWarnings("AlibabaThreadShouldSetName")
    public static ThreadPoolExecutor createThreadPoolExecutor(
            int corePoolSize, int maximumPoolSize, long keepAliveTime, boolean allowCoreThreadTimeout
    ) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1024 * CPUS),
                (r, executor1) ->  {
                    if (!executor1.isShutdown()) {
                        try {
                            executor1.getQueue().put(r);
                        } catch (InterruptedException e) {
                            //ignore
                        }
                    }
                }
        );
        executor.allowCoreThreadTimeOut(allowCoreThreadTimeout);
        return executor;
    }
}
