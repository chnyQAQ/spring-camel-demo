package com.dah.camel.route.timer;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class TimerProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println(exchange.getIn().getBody().toString());
    }
}
