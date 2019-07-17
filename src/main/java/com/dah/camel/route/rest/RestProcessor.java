package com.dah.camel.route.rest;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class RestProcessor implements Processor {

    public static String url_id = "";

    @Override
    public void process(Exchange exchange) throws Exception {
        url_id = exchange.getIn().getHeader("id").toString();
    }

    public static String getUrl_id() {
        return url_id;
    }
}
