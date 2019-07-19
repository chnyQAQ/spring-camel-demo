package com.dah.camel.route.dynimic_formal;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.LinkedList;

public class CustomDynamicRouterProcessor implements Processor {

    private LinkedList<RouteDetail> routeDetails;

    public CustomDynamicRouterProcessor(LinkedList<RouteDetail> routeDetails) {
        this.routeDetails = routeDetails;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader(Exchange.SLIP_ENDPOINT, routeDetails);
    }
}
