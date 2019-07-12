package com.dah.camel.route.redis;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class RedisProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("测试redis");
    }
}
