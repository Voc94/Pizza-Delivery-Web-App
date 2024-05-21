package com.pizzaDeliveryProject.delivery.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    private JedisPool jedisPool;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(50);
        jedisPool = new JedisPool(poolConfig, "localhost", 6379);
        return jedisPool;
    }

    @Bean
    public CommandLineRunner setupFlushDBHook() {
        return args -> {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    flushDB();
                } finally {
                    if (jedisPool != null) {
                        jedisPool.close();
                    }
                }
            }));
        };
    }

    private void flushDB() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.flushDB();
            System.out.println("Flushed Redis DB on application shutdown");
        } catch (Exception e) {
            System.err.println("Error flushing Redis DB on shutdown: " + e.getMessage());
        }
    }
}
