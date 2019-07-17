package com.dah.camel.route.dynimic_formal;

import org.apache.camel.spring.SpringRouteBuilder;

//@Component
public class DirectRouteB extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:directRouteB").routeId("routeB").log("DirectRouteB running...");
    }
}
