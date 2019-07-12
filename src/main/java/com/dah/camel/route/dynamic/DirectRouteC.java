package com.dah.camel.route.dynamic;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class DirectRouteC extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:directRouteC").to("log:DirectRouteC?showExchangeId=true&showProperties=true&showBody=false");
    }
}
