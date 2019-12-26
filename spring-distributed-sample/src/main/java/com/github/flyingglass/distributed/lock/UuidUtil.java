package com.github.flyingglass.distributed.lock;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author fly
 * Created Date 2019/5/17 10:53
 * description: UUID生成器
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class UuidUtil {

    /**
     * 生成uuid
     * @return uuid
     */
    static String genUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
