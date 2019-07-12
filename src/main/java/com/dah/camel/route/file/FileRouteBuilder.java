package com.dah.camel.route.file;

import org.apache.camel.Exchange;
import org.apache.camel.spring.SpringRouteBuilder;

//@Component
public class FileRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:D:\\ftp?delay=10000")
                .routeId("fileRouteBuilder").autoStartup(true)
                .filter().method(this, "filter")
                .to("file:D:\\log");
    }

    public boolean filter(Exchange exchange) {
        System.out.println("测试file");
        return true;
    }
}
