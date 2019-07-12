package com.dah.camel.route.redis;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Component
public class RedisRouteBuilder extends SpringRouteBuilder {

    @Autowired
    private CamelContext camelContext;

    @Override
    public void configure() throws Exception {
        // CamelRedis.Command 命令包含set、publis等
        // CamelRedis.Channel 发布的通道名称
        // CamelRedis.Message 发布到通道中的内容
        /*from("timer://foo?fixedRate=true&period=1000").
                setHeader("CamelRedis.Command", constant("PUBLISH")).
                setHeader("CamelRedis.Channel", constant("testChannel")).
                setHeader("CamelRedis.Message", constant(new Date().toString())).
                routeId("redisRouteBuilder").autoStartup(true).
                to("spring-redis://testName?connectionFactory=#redisConnectionFactory&serializer=#stringSerializer").process(new RedisProcessor());*/

        // 存储
       /*from("timer://foo?fixedRate=true&period=1000").
                setHeader("CamelRedis.Command", constant("SET")).
                setHeader("CamelRedis.Key", constant("测试key")).
                setHeader("CamelRedis.Value", constant("测试Value")).
                to("spring-redis://testName?serializer=#stringSerializer&connectionFactory=#redisConnectionFactory&redisTemplate=#myRedisTemplate").process(new RedisProcessor());*/

        // ttl
        /*from("timer://foo?fixedRate=true&period=1000").
                setHeader("CamelRedis.Command", constant("EXPIRE")).
                setHeader("CamelRedis.Key", constant("测试key1")).
                setHeader("CamelRedis.Timeout", constant(100L)).
                to("spring-redis://testName?connectionFactory=#redisConnectionFactory&redisTemplate=#myRedisTemplate").process(new RedisProcessor());*/

        // 一个from可以有多个to，但是每个to之间必须是一个完整的操作命令,eg:
        /*from("timer://foo?fixedRate=true&period=1000").
                setHeader("CamelRedis.Command", constant("EXPIRE")).
                setHeader("CamelRedis.Key", constant("测试key")).
                setHeader("CamelRedis.Timeout", constant(100L)).

                to("spring-redis://testName?connectionFactory=#redisConnectionFactory&serializer=#stringSerializer").process(new RedisProcessor()).
                setHeader("CamelRedis.Command", constant("SET")).
                setHeader("CamelRedis.Key", constant("测试key")).
                setHeader("CamelRedis.Value", constant("测试Value")).
                to("spring-redis://testName?connectionFactory=#redisConnectionFactory&serializer=#stringSerializer").process(new RedisProcessor());*/

        // 删除，DEL
        from("timer://foo?fixedRate=true&period=10000").
                setHeader("CamelRedis.Command", constant("DEL")).
                setHeader("CamelRedis.Keys", constant("测试key")).
                to("spring-redis://testName?connectionFactory=#redisConnectionFactory&redisTemplate=#myRedisTemplate&serializer=#stringSerializer").process(new RedisProcessor());
    }
}
