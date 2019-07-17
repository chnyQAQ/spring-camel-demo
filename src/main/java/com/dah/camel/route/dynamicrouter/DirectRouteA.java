package com.dah.camel.route.dynamicrouter;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class DirectRouteA extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:directRouteA").routeId("direct:directRouteA").log("DirectRouteA running...");
    }
}
