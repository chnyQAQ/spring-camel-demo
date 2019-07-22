package com.dah.camel.route.dynimic_formal;

import org.apache.camel.*;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class DynamicRouteBuilder extends SpringRouteBuilder {

    @Autowired
    private CamelContext camelContext;

    public static LinkedList<RouteEndpoint> routeEndpointList;

    static {
        routeEndpointList = new LinkedList<>();
        routeEndpointList.add(new RouteEndpoint("routeEndpoint_from","route_1_id", "rest", "endpoint_from_id", "", "", "jetty:http://127.0.0.1:8282/dynamicRouterCamel", "endpoint_from"));
        routeEndpointList.add(new RouteEndpoint("routeEndpoint_1_id","route_1_id", "rest", "endpoint_1_id", "-5 < 0", "routeEndpoint_from","mock:result_endpoint_1_uri", "endpoint_1"));
        routeEndpointList.add(new RouteEndpoint("routeEndpoint_2_id","route_1_id", "rest", "endpoint_2_id", "-5 < 5", "routeEndpoint_from","mock:result_endpoint_2_uri", "endpoint_2"));
        routeEndpointList.add(new RouteEndpoint("routeEndpoint_3_id","route_1_id", "rest", "endpoint_3_id", "-5 > 5", "routeEndpoint_from","mock:result_endpoint_3_uri", "endpoint_3"));
        routeEndpointList.add(new RouteEndpoint("routeEndpoint_4_id","route_1_id", "rest", "endpoint_4_id", "", "routeEndpoint_1_id","mock:result_endpoint_4_uri", "endpoint_4"));
        routeEndpointList.add(new RouteEndpoint("routeEndpoint_5_id","route_1_id", "rest", "endpoint_5_id", "", "routeEndpoint_1_id","mock:result_endpoint_5_uri", "endpoint_5"));
        routeEndpointList.add(new RouteEndpoint("routeEndpoint_6_id","route_1_id", "rest", "endpoint_6_id", "", "routeEndpoint_4_id","mock:result_endpoint_6_uri", "endpoint_6"));
        routeEndpointList.add(new RouteEndpoint("routeEndpoint_7_id","route_1_id", "rest", "endpoint_7_id", "", "routeEndpoint_4_id","mock:result_endpoint_7_uri", "endpoint_7"));
        routeEndpointList.add(new RouteEndpoint("routeEndpoint_8_id","route_1_id", "rest", "endpoint_8_id", "", "routeEndpoint_5_id","mock:result_endpoint_8_uri", "endpoint_8"));
    }

    @Override
    public void configure() throws Exception {
        RouteEndpoint routeEndpointFrom = RouteEndpointUtils.getRouteEndpointFrom(routeEndpointList);
        // 循环动态路由 Dynamic Router
        LinkedList<RouteEndpoint> routeEndpointReceiverListTemp = RouteEndpointUtils.getMulticastLists(RouteEndpointUtils.getReceiverListBySenderId(routeEndpointFrom.getId(), routeEndpointList));

        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.from(routeEndpointFrom.getEndpointUri())
                .routeId(routeEndpointFrom.getEndpointName()).autoStartup(false)
                .process(new CustomDynamicRouterProcessor(routeEndpointReceiverListTemp))
                .dynamicRouter().method(this, "choiceRoute");
        camelContext.addRouteDefinition(routeDefinition);

        RouteDefinition routeDefinition1 = new RouteDefinition();
        routeDefinition1.from("jetty:http://127.0.0.1:8282/dynamicRouterCamel1")
                .routeId("testStop").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                Route route = camelContext.getRoute("endpoint_4");
                if(route != null) {
                    camelContext.stopRoute("endpoint_4", 500, TimeUnit.MICROSECONDS);
                    camelContext.removeRoute("endpoint_4");
                }
            }
        }).log("test stop!");
        camelContext.addRouteDefinition(routeDefinition1);

        RouteDefinition routeDefinition2 = new RouteDefinition();
        routeDefinition2.from("jetty:http://127.0.0.1:8282/dynamicRouterCamel2")
                .routeId("testStart").process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                Route route = camelContext.getRoute("endpoint_4");
                LinkedList<RouteEndpoint> listTemp = RouteEndpointUtils.getReceiverListBySenderId("routeEndpoint_4_id", routeEndpointList);
                if(route == null) {
                    RouteDefinition routeDefinition3 = new RouteDefinition();
                    routeDefinition3.from(RouteEndpoint.PREFIX_DIRECT + "endpoint_4")
                            .routeId("endpoint_4")
                            .to("mock:result").process(new CustomDynamicRouterProcessor(listTemp))
                            .dynamicRouter().method(DynamicRouteBuilder.class, "choiceRoute")
                            .log("from: " + listTemp.getFirst().getPreviousId() + " to: endpoint_4 running..." + System.currentTimeMillis());
                    camelContext.addRouteDefinition(routeDefinition3);
                }
            }
        }).log("test start!");
        camelContext.addRouteDefinition(routeDefinition2);

        initRoutes(routeEndpointReceiverListTemp);
        //camelContext.startRoute(routeEndpointFrom.getId());
    }

    // 初始化路由
    public void initRoutes (LinkedList<RouteEndpoint> routeEndpoints) throws Exception {
        if(!EmptyUtil.isEmptyList(routeEndpoints)){
            for (RouteEndpoint routeEndpoint : routeEndpoints) {
                LinkedList<RouteEndpoint> routeEndpointLinkedList = RouteEndpointUtils.getMulticastLists(RouteEndpointUtils.getReceiverListBySenderId(routeEndpoint.getId(), routeEndpointList));
                buildRoute(routeEndpoint);
                initRoutes(routeEndpointLinkedList);
            }
            buildMulticast(routeEndpoints);
        }
    }

    // 构建广播
    public void buildMulticast(LinkedList<RouteEndpoint> routeEndpoints) throws Exception {
        if(!EmptyUtil.isEmptyList(routeEndpoints)){
            //广播临时名称
            int len = routeEndpoints.size();
            String[] array = new String[len];
            for (int i=0; i< len; i++){
                //判断当前上下文中是否还存在当前路由的广播，存在则删除，重新构建
                RouteEndpoint routeEndpoint = routeEndpoints.get(i);
                Route route = camelContext.getRoute(RouteEndpoint.PREFIX_MULTICAST + routeEndpoint.getPreviousId());
                if(null != route){
                    camelContext.stopRoute(route.getId(), 500, TimeUnit.MICROSECONDS);
                    camelContext.removeRoute(route.getId());
                }
                array[i] = RouteEndpoint.PREFIX_DIRECT + routeEndpoints.get(i).getEndpointName();
                RouteEndpointUtils.replaceByRouteEndpoint(routeEndpoint, routeEndpointList);
            }

            RouteDefinition routeDefinition = new RouteDefinition();
            routeDefinition.from(RouteEndpoint.PREFIX_DIRECT + RouteEndpoint.PREFIX_MULTICAST + routeEndpoints.getFirst().getPreviousId())
                    .routeId(RouteEndpoint.PREFIX_MULTICAST + routeEndpoints.getFirst().getPreviousId())
                    .multicast()//广播
                    .parallelProcessing(true)//同时向多播发送消息。请注意，调用方 线程仍将等待，直到所有消息都已完全处理完毕，然后再继续。它只发送和处理同时发生的多播的回复。
                    .log("from: " + RouteEndpoint.PREFIX_MULTICAST + routeEndpoints.getFirst().getPreviousId() + " to: " + array[0] + " running..." + System.currentTimeMillis())
                    .to(array);
            camelContext.addRouteDefinition(routeDefinition);
        }
    }
    // 构建
    public void buildRoute(RouteEndpoint routeEndpoint) throws Exception {
        //获取当前节点的接收者列表
        LinkedList<RouteEndpoint> routeEndpointLinkedList = RouteEndpointUtils.getMulticastLists(RouteEndpointUtils.getReceiverListBySenderId(routeEndpoint.getId(), routeEndpointList));
        Route route = camelContext.getRoute(routeEndpoint.getEndpointName());
        // 判定上下文中是否存在route,不存在则创建route
        if (null == route) {
            RouteDefinition routeDefinition = new RouteDefinition();
            routeDefinition.from(RouteEndpoint.PREFIX_DIRECT + routeEndpoint.getEndpointName())
                    .routeId(routeEndpoint.getEndpointName())
                    .to(routeEndpoint.getEndpointUri())
                    .process(new CustomDynamicRouterProcessor(routeEndpointLinkedList))
                    .dynamicRouter().method(this, "choiceRoute")
                    .log("from: " + routeEndpoint.getPreviousId() + " to: " + routeEndpoint.getEndpointName() + " running..." + System.currentTimeMillis());
            camelContext.addRouteDefinition(routeDefinition);
        }
    }

    // 选择路由执行
    public String choiceRoute(@Header(Exchange.SLIP_ENDPOINT) LinkedList<RouteEndpoint> routeEndpoints) throws Exception {
        if(!EmptyUtil.isEmptyList(routeEndpoints)) {
            return RouteEndpoint.PREFIX_DIRECT + RouteEndpoint.PREFIX_MULTICAST + routeEndpoints.getFirst().getPreviousId();
        }
        return null;
    }
}
