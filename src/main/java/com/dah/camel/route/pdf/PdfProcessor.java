package com.dah.camel.route.pdf;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class PdfProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println(exchange.getIn().getBody().toString());
    }
}
