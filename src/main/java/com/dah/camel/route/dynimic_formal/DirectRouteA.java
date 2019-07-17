package com.dah.camel.route.dynimic_formal;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class DirectRouteA extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:endpoint_1").routeId("direct:endpoint_1").log("endpoint_1 running...");
    }
}
