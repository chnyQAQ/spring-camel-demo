package com.dah.camel.route.dynimic_formal;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;


public class CustomDynamicRouterProcessor implements Processor {

    private List<RouteEndpoint> routeEndpoints;

    public CustomDynamicRouterProcessor(List<RouteEndpoint> routeEndpoints) {
        this.routeEndpoints = routeEndpoints;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader(Exchange.SLIP_ENDPOINT, routeEndpoints);
    }
}
