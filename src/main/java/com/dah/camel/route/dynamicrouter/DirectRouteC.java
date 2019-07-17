package com.dah.camel.route.dynamicrouter;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class DirectRouteC extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:directRouteC").routeId("routeC").log("DirectRouteC running...");
    }
}
