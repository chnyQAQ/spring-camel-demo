package com.dah.camel.route.multicast;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MulticastRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from("jetty:http://127.0.0.1:8282/dynamicRouterCamel")
                .multicast()
                .parallelProcessing()//同时向多播发送消息。请注意，调用方 线程仍将等待，直到所有消息都已完全处理完毕，然后再继续。它只发送和处理同时发生的多播的回复。
                .to("direct:directRouteA","direct:directRouteB","direct:directRouteC","direct:directRouteD",
                        "direct:directRouteE","direct:directRouteF","direct:directRouteG","direct:directRouteH",
                        "direct:directRouteI","direct:directRouteB");

        from("direct:directRouteA")
                .to("direct:directRouteK")
                .log("from: directRouteA" + System.currentTimeMillis());

        from("direct:directRouteB")
                .log("from: directRouteB" + System.currentTimeMillis());

        from("direct:directRouteC")
                .log("from: directRouteC" + System.currentTimeMillis());

        from("direct:directRouteD")
                .log("from: directRouteD" + System.currentTimeMillis());

        from("direct:directRouteE")
                .log("from: directRouteE" + System.currentTimeMillis());

        from("direct:directRouteF")
                .log("from: directRouteF" + System.currentTimeMillis());

        from("direct:directRouteG")
                .log("from: directRouteG" + System.currentTimeMillis());

        from("direct:directRouteH")
                .log("from: directRouteH" + System.currentTimeMillis());

        from("direct:directRouteI")
                .log("from: directRouteI" + System.currentTimeMillis());

        from("direct:directRouteJ")
                .log("from: directRouteJ" + System.currentTimeMillis());

        from("direct:directRouteK")
                .to("direct:directRouteJ")
                .log("from: directRouteK" + System.currentTimeMillis());


    }
}
