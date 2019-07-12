package com.dah.camel.route.dynamic;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class DirectRouteB extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:directRouteB").to("log:DirectRouteB?showExchangeId=true&showProperties=true&showBody=false");
    }
}
