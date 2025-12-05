package com.smart_system_monitor.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
    @Value("{$redis.url}")
    private String HOST;
    
    private final int PORT = 14176;
    private final String USER = "default";

    @Value("{$redis.password}")
    private String PASSWORD;

    @Bean
    public JedisPool jedisPool(){
        return new JedisPool(new JedisPoolConfig(), HOST, PORT, USER, PASSWORD);
    }
}
