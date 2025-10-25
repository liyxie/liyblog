package com.liy.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis连接健康检查类
 * 定期检查Redis连接状态，并在连接异常时记录日志
 */
@Component
@Slf4j
public class RedisHealthCheck {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    private static final String HEALTH_CHECK_KEY = "redis:health:check";
    private static final String HEALTH_CHECK_VALUE = "OK";

    /**
     * 每30秒检查一次Redis连接状态
     */
    @Scheduled(fixedRate = 30000)
    public void checkRedisConnection() {
        try {
            // 执行简单的PING命令
            String result = redisTemplate.getConnectionFactory().getConnection().ping();
            if ("PONG".equals(result)) {
                // 连接正常，记录健康状态
                redisTemplate.opsForValue().set(HEALTH_CHECK_KEY, HEALTH_CHECK_VALUE, 60, TimeUnit.SECONDS);
                log.debug("Redis连接健康检查通过");
            }
        } catch (Exception e) {
            log.warn("Redis连接健康检查失败: {}", e.getMessage());
            // 尝试重新建立连接
            try {
                redisTemplate.getConnectionFactory().getConnection().close();
                log.info("Redis连接已重置");
            } catch (Exception ex) {
                log.error("Redis连接重置失败: {}", ex.getMessage());
            }
        }
    }

    /**
     * 检查Redis连接是否活跃
     */
    public boolean isRedisConnectionActive() {
        try {
            String value = redisTemplate.opsForValue().get(HEALTH_CHECK_KEY);
            return HEALTH_CHECK_VALUE.equals(value);
        } catch (Exception e) {
            log.warn("检查Redis连接状态失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取Redis连接信息
     */
    public String getRedisConnectionInfo() {
        try {
            return String.format("Redis连接状态: %s, 连接工厂: %s", 
                    isRedisConnectionActive() ? "正常" : "异常",
                    redisConnectionFactory.getClass().getSimpleName());
        } catch (Exception e) {
            return "Redis连接信息获取失败: " + e.getMessage();
        }
    }
}