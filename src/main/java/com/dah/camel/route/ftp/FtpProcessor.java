package com.dah.camel.route.ftp;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class FtpProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Exchange tempExchange = exchange;
        tempExchange.getProperties();
        System.out.println("测试ftp");
    }
}
