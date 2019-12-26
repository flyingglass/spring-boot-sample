package com.github.flyingglass.distributed.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisCommands;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author: fly
 * Created date: 2019/12/26 17:51
 * Redis 分布式简易锁
 * 命令 SET authentication-name anystring NX EX max-lock-time 是一种在 Redis 中实现锁的简单方法
 * https://www.jianshu.com/p/30009132a295
 *
 */
@Slf4j
public class RedisSampleLock implements Lock {

    private String lockKey;
    private Long expireTime;
    private String lockValue;
    private RedisTemplate<String, String> redisTemplate;


    private volatile boolean locked = false;
    private static final String OK = "ok";

    /**
     * 解锁的lua脚本
     */
    public static final String UNLOCK_LUA;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    public RedisSampleLock(RedisTemplate<String, String> redisTemplate, String lockKey, Long expireTime) {
        this.redisTemplate = redisTemplate;
        this.lockKey = lockKey;
        this.expireTime = expireTime;
    }

    @Override
    public void lock() {
        lockValue = UuidUtil.genUUID();
        do {
            String result = set(lockKey, lockValue, expireTime);
            if (OK.equalsIgnoreCase(result)) {
                locked = true;
            }
            sleepWrap(10, 50000);
        } while (true);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        lockValue = UuidUtil.genUUID();
        return OK.equalsIgnoreCase(set(lockKey, lockValue, expireTime));
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        lockValue = UuidUtil.genUUID();
        long now = System.nanoTime();
        do {
            if (OK.equalsIgnoreCase(set(lockKey, lockValue, expireTime))) {
                locked = true;
                // 上锁成功结束
                return true;
            }
        } while ((System.nanoTime() - now) <  unit.toNanos(time));
        return false;
    }

    @Override
    public void unlock() {
        if (locked) {
            redisTemplate.execute((RedisCallback<Boolean>) connection -> {
                Object nativeConnection = connection.getNativeConnection();

                Long result = 0L;
                // cluster
                if (nativeConnection instanceof JedisCluster) {
                    result = (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, Collections.singletonList(lockKey), Collections.singletonList(lockValue));
                }

                // single host
                if (nativeConnection instanceof Jedis) {
                    result = (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, Collections.singletonList(lockKey), Collections.singletonList(lockValue));
                }

                if (result == 0) {
                    log.error("解锁失败:{}", System.currentTimeMillis());
                }

                return locked = result == 0;
            });
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    private String set(final String key, final String value, final long msec) throws DataAccessException {
        if (StringUtils.isEmpty(key)) {
            log.error("key不能为空");
            return null;
        }
        return redisTemplate.execute((RedisCallback<String>) connection -> {
            Object nativeConnection = connection.getNativeConnection();
            String result = null;

            // 全部用lua_script比较合理
            if (nativeConnection instanceof JedisCommands) {
                result = ((JedisCommands) nativeConnection).set(key, value, new SetParams().nx().px(msec));
            }
            if (!StringUtils.isEmpty(result)) {
                log.info("获取锁的时间:{}", System.currentTimeMillis());
            }
            return result;
        });
    }

    /**
     * sleep休眠
     * @param msec
     * @param nanos
     */
    private void sleepWrap(long msec, int nanos) {
        try {
            Thread.sleep(msec, nanos);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("获取分布式锁休眠被中断", e);
        }
    }
}
