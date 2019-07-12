package com.dah.camel.route.timer;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class TimerRouteBuilder extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {


        //repeatCount 重复次数   0 无限重复
        //delay 延迟
        //time 设置指定时间执行 yyyy-MM-dd HH:mm:ss 或 yyyy-MM-dd 'T' HH:mm:ss

        //firedTime 消费者解雇时间 exchange.in.headers里的一个属性
        /*from("timer:chny?period=2s").routeId("timer1")
                .setBody().simple("Current time is ${header.firedTime}")
                .to("log:TimerServerOutMessage?showExchangeId=true")
                .log("${body}");*/

        //autoStartup(false) 是否自动开启
        from("timer:start?period=2s").routeId("start").autoStartup(false)
                .to("direct:hello-user")
                .log("${body}");

        from("timer:hello?period=10s").routeId("hello-user")
                .to("direct:hello-user")
                .log("${body}");

        from("direct:hello-user")
                .to("jetty:http://127.0.0.1:8001/start");

    }

}
