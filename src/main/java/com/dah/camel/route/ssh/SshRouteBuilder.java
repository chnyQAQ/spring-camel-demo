package com.dah.camel.route.ssh;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class SshRouteBuilder extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        //ssh:[username[:password]@]host[:port][?options]
        from("ssh://root:dah@2019@192.167.9.233:22?useFixedDelay=true&delay=5000&pollCommand=feature:list%0A").log("${body}");
    }
}
