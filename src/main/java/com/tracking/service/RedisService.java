package com.tracking.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@Service
public class RedisService {
    private JedisPool jedisPool;
    private boolean isRedisAvailable = false;

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Value("${spring.data.redis.password:}")
    private String redisPassword;

    @PostConstruct
    public void init() {
        try {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(8);
            poolConfig.setMaxIdle(8);
            poolConfig.setMinIdle(0);
            poolConfig.setTestOnBorrow(true);
            poolConfig.setTestOnReturn(true);
            poolConfig.setTestWhileIdle(true);
            poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
            poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
            poolConfig.setNumTestsPerEvictionRun(3);
            poolConfig.setBlockWhenExhausted(true);

            if (redisPassword != null && !redisPassword.isEmpty()) {
                jedisPool = new JedisPool(poolConfig, redisHost, redisPort, 2000, redisPassword);
            } else {
                jedisPool = new JedisPool(poolConfig, redisHost, redisPort);
            }

            // Test Redis connection
            try (Jedis jedis = jedisPool.getResource()) {
                String pong = jedis.ping();
                isRedisAvailable = "PONG".equals(pong);
                if (isRedisAvailable) {
                    log.info("Redis connection established successfully");
                }
            }
        } catch (Exception e) {
            log.warn("Redis is not available: {}", e.getMessage());
            isRedisAvailable = false;
        }
    }

    public boolean setIfNotExists(String key, String value, int expireSeconds) {
        if (!isRedisAvailable) {
            log.warn("Redis is not available, skipping setIfNotExists operation");
            return true; // Return true to allow tracking number generation to proceed
        }

        try (Jedis jedis = jedisPool.getResource()) {
            SetParams params = SetParams.setParams()
                    .nx()  // Only set if key does not exist
                    .ex(expireSeconds);  // Set expiration in seconds
            return "OK".equals(jedis.set(key, value, params));
        } catch (Exception e) {
            log.error("Redis operation failed: {}", e.getMessage());
            return true; // Return true to allow tracking number generation to proceed
        }
    }

    @PreDestroy
    public void destroy() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }

    public boolean isRedisAvailable() {
        return isRedisAvailable;
    }
} 