package com.liy.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis连接测试类
 * 应用启动时测试Redis连接是否正常
 */
@Component
@Slf4j
public class RedisConnectionTest implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisHealthCheck redisHealthCheck;

    private static final String TEST_KEY = "redis:connection:test";
    private static final String TEST_VALUE = "CONNECTION_TEST_SUCCESS";

    @Override
    public void run(String... args) throws Exception {
        log.info("开始测试Redis连接...");
        
        try {
            // 测试基本连接
            String pingResult = redisTemplate.getConnectionFactory().getConnection().ping();
            log.info("Redis PING测试结果: {}", pingResult);
            
            // 测试读写操作
            redisTemplate.opsForValue().set(TEST_KEY, TEST_VALUE, 60, TimeUnit.SECONDS);
            Object result = redisTemplate.opsForValue().get(TEST_KEY);
            
            if (TEST_VALUE.equals(result)) {
                log.info("Redis读写测试成功");
            } else {
                log.warn("Redis读写测试失败，期望值: {}, 实际值: {}", TEST_VALUE, result);
            }
            
            // 测试连接健康检查
            boolean isHealthy = redisHealthCheck.isRedisConnectionActive();
            log.info("Redis连接健康状态: {}", isHealthy ? "正常" : "异常");
            
            // 输出连接信息
            log.info("Redis连接信息: {}", redisHealthCheck.getRedisConnectionInfo());
            
            log.info("Redis连接测试完成");
            
        } catch (Exception e) {
            log.error("Redis连接测试失败: {}", e.getMessage(), e);
            log.warn("Redis连接可能存在问题，但应用将继续运行");
        }
    }
}