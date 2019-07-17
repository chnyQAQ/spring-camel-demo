package com.dah.camel.route.dynamicrouter;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class DirectRouteB extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:directRouteB").routeId("routeB").log("DirectRouteB running...");
    }
}
