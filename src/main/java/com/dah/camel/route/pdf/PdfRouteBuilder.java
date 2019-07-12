package com.dah.camel.route.pdf;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class PdfRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        //暂未成功
        //from("file:C:\\Users\\Administrator\\Desktop\\test.pdf").setBody().simple("Test Chny!").to("pdf:extractText").log("${body}");

        //from("file:C:\\Users\\Administrator\\Desktop\\test.pdf").setBody().simple("Test Chny!").to("pdf:append").log("${body}");
    }

}
