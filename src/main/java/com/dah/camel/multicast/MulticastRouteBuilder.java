package com.dah.camel.multicast;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class MulticastRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from("jetty:http://127.0.0.1:8282/dynamicRouterCamel")
                .multicast()
                .to("direct:directRouteA","direct:directRouteB");

        from("direct:directRouteA")
                .log("from: directRouteA" + System.currentTimeMillis());
        from("direct:directRouteB")
                .log("from: directRouteB" + System.currentTimeMillis());

    }
}
