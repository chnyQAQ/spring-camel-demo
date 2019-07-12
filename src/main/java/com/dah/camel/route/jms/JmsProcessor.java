package com.dah.camel.route.jms;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class JmsProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("测试jms: " + exchange.getIn().getMessageId());
    }
}
