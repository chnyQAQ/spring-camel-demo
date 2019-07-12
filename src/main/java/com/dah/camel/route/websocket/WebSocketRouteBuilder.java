package com.dah.camel.route.websocket;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class WebSocketRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        //from("jmstest:queue:prober.status.realtime") .routeId("webSocketRouteBuilder") .to("websocket://localhost:8443/newsTopic?sendToAll=true");
        from("timer://queryAward?period=100").routeId("webSocketRouteBuilder").setBody(constant("测试websocket")).to("websocket://localhost:8443/newsTopic?sendToAll=true") .to("jmstest:queue:prober.status.realtime");
    }
}
