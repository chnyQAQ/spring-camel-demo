package com.dah.camel.route.dynamic;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;

public class DynamicRouteProcessor implements Processor {

    @Autowired
    private CamelContext camelContext;

    @Override
    public void process(Exchange exchange) throws Exception {
        String queryString = exchange.getIn().getHeader(Exchange.HTTP_URL, String.class);
        queryString = queryString.substring(queryString.lastIndexOf("/") + 1, queryString.length());
        if (queryString.equals("1")){
            camelContext.startRoute("ftpRouteBuilder");
            System.out.println("开启ftpRouteBuilder");
        } else if(queryString.equals("2")){
            camelContext.startRoute("jdbcRouteBuilder");
            System.out.println("开启jdbcRouteBuilder");
        }

    }
}
