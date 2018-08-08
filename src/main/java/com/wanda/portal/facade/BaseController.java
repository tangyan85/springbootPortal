package com.wanda.portal.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 公用控制器
 *
 * @author cloud He
 * @since 1.0.0 2018/8/7
 */
public class BaseController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 缓存对象默认5分钟
     *
     * @param key
     * @param func
     * @param <R>
     * @return
     */
    protected <R> R getForCache(String key, Supplier<R> func) {
        R result;
        long expireTime = redisTemplate.getExpire(key);

        if (expireTime <= 0) {
            result = func.get();
            if (result != null) {
                redisTemplate.opsForList().leftPush(key, result);
                redisTemplate.expire(key, 5, TimeUnit.MINUTES);
            }
        } else {
            result = (R) redisTemplate.opsForList().leftPop(key);
        }

        return result;
    }

}
