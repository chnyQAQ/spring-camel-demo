package com.dah.camel.route.dynimic_formal;

import org.apache.camel.spring.SpringRouteBuilder;

//@Component
public class DirectRouteC extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:directRouteC").routeId("routeC").log("DirectRouteC running...");
    }
}
