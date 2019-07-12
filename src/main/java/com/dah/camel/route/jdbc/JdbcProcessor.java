package com.dah.camel.route.jdbc;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class JdbcProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        System.out.println("测试jdbc: " + exchange.getIn().getBody().toString());
    }
}
