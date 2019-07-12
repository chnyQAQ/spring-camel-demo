package com.dah.camel.route.tcp;

import org.apache.camel.ExchangeTimedOutException;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class TcpRouteBuilder extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {

        //mina2:protocol:host:port
        //mina2:tcp://hostname[:port][?options]
        //mina2:udp://hostname[:port][?options]
        //mina2:vm://hostname[:port][?options]
        //from("mina2:tcp://127.0.0.1:8081").routeId("mina2-tcp").log("${body}");

        //netty4:tcp:host:port
        //netty4:tcp://0.0.0.0:99999[?options]
        //netty4:udp://remotehost:99999/[?options]
        //from("netty4:tcp://127.0.0.1:8082").routeId("netty4-tcp").log("${body}");

        //此异常处理需要放在所有from前面
        //为超时构建一个特殊的自定义错误消息
        /*onException(ExchangeTimedOutException.class)
                //持续运行route
                .continued(true)
                .setBody(simple("#${header.firedTime}-Time out error!!!"));*/

        from("netty4:tcp://127.0.0.1:4444?sync=true")
                .log("Request:  ${id}:${body}")
                //设置1-9s的随机延迟
                .delay(simple("${random(1000,9000)}")).asyncDelayed().end()
                .end()
                .transform(simple("${body}-Echo"))
                .log("Response: ${id}:${body}");


        from("timer:trigger")
                .setBody(simple("#${header.firedTime}"))
                .log("Request:  ${id}:${body}")
                .to("netty4:tcp://127.0.0.1:4444?sync=true&producerPoolEnabled=false")
                .log("Response: ${id}:${body}");
    }


}
