package com.smart_system_monitor.aspects;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.smart_system_monitor.models.ProcessLogItem;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Aspect
@Component
public class LoggingAspect {
    private JedisPool jedisPool;

    private Gson gson;

    @Autowired
    private LoggingAspect(JedisPool jedisPool, Gson gson){
        this.jedisPool = jedisPool;
        this.gson = gson;
    }
    
    @Pointcut("execution(* com.smart_system_monitor.services..*(..))")
    private void loggingPointcutSignature(){}

    /**
     * logging advice to store process action entries in redis
    */
    @AfterReturning(value= "loggingPointcutSignature()", returning= "logItem")
    public void logToRedis(ProcessLogItem logItem){
        System.out.println("log");
        try(Jedis jedis = jedisPool.getResource()){
            String logItemJSON = gson.toJson(logItem);

            // store entry in redis
            jedis.set(String.valueOf(logItem.getPid()), logItemJSON);
            System.out.println("Logged new item " + logItemJSON);
        }
    }
}
