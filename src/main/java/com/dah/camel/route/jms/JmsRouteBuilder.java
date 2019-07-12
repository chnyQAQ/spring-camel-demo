package com.dah.camel.route.jms;

import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;

//@Component
public class JmsRouteBuilder extends SpringRouteBuilder {

    @Autowired
    CamelContext camelContext;

    @Autowired
    ConnectionFactory connectionFactory;

    private void initJmsProperties() {
        // 下面使用时只需要对应上名称即可
        camelContext.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        camelContext.addComponent("jms1", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
    }

    @Override
    public void configure() throws Exception {
        initJmsProperties();
        // 队列
        // 可指定消费者数量 concurrentConsumers
       /* from("jms:queue:prober.status.realtime?concurrentConsumers=5").process(new JmsProcessor());

        from("jms1:queue:prober.status.realtime?concurrentConsumers=5").process(new JmsProcessor());*/
        // 配置消息格式转换器，注意转换器名称前的‘#’
        from("jms:queue:prober.status.realtime?concurrentConsumers=1&messageConverter=#jmsMessageConverter").routeId("jmsRouteBuilder").autoStartup(true).process(new JmsProcessor());

        // topic
       /* from("jms:topic:prober.status.realtime?concurrentConsumers=5").process(new JmsProcessor());*/
    }
}
