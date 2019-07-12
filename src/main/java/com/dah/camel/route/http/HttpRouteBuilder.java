package com.dah.camel.route.http;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class HttpRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from("jetty:http://localhost:8080/myapp/myservice").to("https://www.baidu.com?bridgeEndpoint=true");
        from("jetty:http://localhost:8080/myapp/myservice1").to("http://localhost:8001/hello?bridgeEndpoint=true");
        from("jetty:http://localhost:8080/myapp/myservice2").to("direct:testRoute");

        from("direct:testRoute").to("https://www.baidu.com?bridgeEndpoint=true");
    }
}
