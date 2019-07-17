package com.dah.camel.route.dynamic;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

//@Component
public class DynamicRouteBuilder extends SpringRouteBuilder {

    @Autowired
    private CamelContext camelContext;

    @Override
    public void configure() throws Exception {

        /*from("jetty:http://localhost:8080/dynamicRouteBuilder").routeId("dynamicRouteBuilder")
                .choice()
                .when("").to("")
                .when("").to("")
                .otherwise()
                .to("")
                .endChoice();*/

        /*from("jetty:http://localhost:8080/dynamicRouteBuilder/{test}").routeId("dynamicRouteBuilder").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                String queryString = exchange.getIn().getHeader(Exchange.HTTP_URL, String.class);
                queryString = queryString.substring(queryString.lastIndexOf("/") + 1, queryString.length());
                if (queryString.equals("1")) {
                    camelContext.startRoute("ftpRouteBuilder");
                    System.out.println("开启ftpRouteBuilder");
                } else if (queryString.equals("2")) {
                    camelContext.startRoute("jdbcRouteBuilder");
                    System.out.println("开启jdbcRouteBuilder");
                } else if (queryString.equals("3")) {
                    RouteDefinition routeDefinition = new RouteDefinition();
                    routeDefinition.from("jetty:http://localhost:8080/myapp/myservice3").routeId("createdRouteBuilder").to("http://localhost:8001/test1?bridgeEndpoint=true");
                    Processor processor = new Processor() {
                        @Override
                        public void process(Exchange exchange) throws Exception {

                        }
                    };
                    routeDefinition.process(processor);
                    camelContext.addRouteDefinition(routeDefinition);
                    System.out.println("创建createdRouteBuilder");
                }
            }
        });*/

        // 循环动态路由 Dynamic Router
        from("jetty:http://127.0.0.1:8282/dynamicRouterCamel")
                // 使用dynamicRouter，进行“动态路由”循环，
                // 直到指定的下一个元素为null为止
                .dynamicRouter().method(this, "doDirect")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println(exchange.getIn());
                    }
        });
    }

    public String doDirect(@ExchangeProperties Map<String, Object> properties) {
        System.out.println("properties : " + properties);
        // 在Exchange的properties属性中，取出Dynamic Router的循环次数
        AtomicInteger time = (AtomicInteger) properties.get("time");
        if (time == null) {
            time = new AtomicInteger(0);
            properties.put("time", time);
        } else {
            time = (AtomicInteger) time;
        }
        System.out.println("这是Dynamic Router循环第：【" + time.incrementAndGet() + "】次执行！执行线程：" + Thread.currentThread().getName());
        // 第一次选择DirectRouteB
        if (time.get() == 1) {
            return "direct:directRouteB";
        }
        // 第二次选择DirectRouteC
        else if (time.get() == 2) {
            return "direct:directRouteC";
        }
        // 其它情况返回null，终止 dynamicRouter的执行
        return null;
    }



    /**
     *  from + isMulticast
     *
     */
        /*from("jetty:http://127.0.0.1:8282/dynamicRouterCamelAll")
                //.to("direct:directRouteAll");
                // 使用dynamicRouter，进行“动态路由”循环，
                // 直到指定的下一个元素为null为止
                .dynamicRouter().method(this, "dynamicDirectAll");

        from("direct:directRouteAll")
                .multicast()
                .to((String[]) list.toArray());*/
}
