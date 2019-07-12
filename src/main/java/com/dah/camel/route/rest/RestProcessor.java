package com.dah.camel.route.rest;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class RestProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("in:"+exchange.getIn().getBody().toString());
        //System.out.println("out:"+exchange.getOut().getBody().toString());
    }
}
