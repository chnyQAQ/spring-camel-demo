package com.dah.camel.route.dynimic_formal;

import org.apache.camel.*;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class Test extends SpringRouteBuilder {

    @Autowired
    private CamelContext camelContext;

    public static LinkedList<RouteDetail> routeDetails;

    static {
        routeDetails = new LinkedList<>();
        routeDetails.add(new RouteDetail("routeDetail_from","route_1_id", "rest", "endpoint_from_id", "", "", "route_from","jetty:http://127.0.0.1:8282/dynamicRouterCamel", "endpoint_from", true, false,null));
        routeDetails.add(new RouteDetail("routeDetail_1_id","route_1_id", "rest", "endpoint_1_id", "-5 < 0", "routeDetail_from", "route_from","mock:result_endpoint_1_uri", "endpoint_1", true, false,null));
        routeDetails.add(new RouteDetail("routeDetail_2_id","route_1_id", "rest", "endpoint_2_id", "-5 < 5", "routeDetail_from", "route_from","mock:result_endpoint_2_uri", "endpoint_2", false, false,null));
        routeDetails.add(new RouteDetail("routeDetail_3_id","route_1_id", "rest", "endpoint_3_id", "-5 > 5", "routeDetail_from", "route_from","mock:result_endpoint_3_uri", "endpoint_3", false, false,null));
        routeDetails.add(new RouteDetail("routeDetail_4_id","route_1_id", "rest", "endpoint_4_id", "", "routeDetail_1_id", "route_from","mock:result_endpoint_4_uri", "endpoint_4", false, false,null));
        routeDetails.add(new RouteDetail("routeDetail_5_id","route_1_id", "rest", "endpoint_5_id", "", "routeDetail_1_id", "route_from","mock:result_endpoint_5_uri", "endpoint_5", false, false,null));
        routeDetails.add(new RouteDetail("routeDetail_6_id","route_1_id", "rest", "endpoint_6_id", "", "routeDetail_4_id", "route_from","mock:result_endpoint_6_uri", "endpoint_6", false, false,null));
        routeDetails.add(new RouteDetail("routeDetail_7_id","route_1_id", "rest", "endpoint_7_id", "", "routeDetail_4_id", "route_from","mock:result_endpoint_7_uri", "endpoint_7", false, false,null));
    }

    @Override
    public void configure() throws Exception {
        RouteDetail routeDetailFrom = RouteDetailUtils.getRouteDetailFrom(routeDetails);
        // 循环动态路由 Dynamic Router
        LinkedList<RouteDetail> routeDetailReceiverListTemp = RouteDetailUtils.getMulticastLists(RouteDetailUtils.getReceiverListBySenderId(routeDetailFrom.getId(), routeDetails));
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.from(routeDetailFrom.getEndpointUri())
            .routeId(routeDetailFrom.getEndpointName())
            .process(new CustomDynamicRouterProcessor(routeDetailReceiverListTemp))
            .dynamicRouter().method(Test.class, "choiceRoute");
        camelContext.addRouteDefinition(routeDefinition);
        initRoutes(routeDetailReceiverListTemp);
        camelContext.startRoute(routeDetailFrom.getId());
    }



    // 初始化路由
    public void initRoutes (List<RouteDetail> routeDetailList) throws Exception {
        if(!EmptyUtil.isEmptyList(routeDetailList)){
            for (RouteDetail routeDetail : routeDetailList) {
                LinkedList<RouteDetail> routeDetailReceiverListTemp = RouteDetailUtils.getMulticastLists(RouteDetailUtils.getReceiverListBySenderId(routeDetail.getId(), routeDetails));
                buildRoute(routeDetail);
                initRoutes(routeDetailReceiverListTemp);
                buildMulticast(routeDetailList);
            }
        }
    }

    // 构建广播
    public void buildMulticast(List<RouteDetail> routeDetailList) throws Exception {
        if(!EmptyUtil.isEmptyList(routeDetailList)){
            //广播临时名称
            String name = "multicast_" + UUID.randomUUID().toString();
            int len = routeDetailList.size();
            String [] array = new String[len];
            for (int i=0; i< len; i++){
                //判断当前上下文中是否还存在当前路由的广播，存在则删除，重新构建
                RouteDetail routeDetail = routeDetailList.get(i);
                Route route = camelContext.getRoute(routeDetail.getMulticast());
                if(null != route){
                    camelContext.stopRoute(route.getId(), 500, TimeUnit.MICROSECONDS);
                    camelContext.removeRoute(route.getId());
                }
                array[i] = RouteDetail.PREFIX_DIRECT + routeDetailList.get(i).getEndpointName();
                routeDetail.setMulticast(name);
                RouteDetailUtils.replaceByRouteDetail(routeDetail, routeDetails);
            }

            RouteDefinition routeDefinition = new RouteDefinition();
            routeDefinition.from(RouteDetail.PREFIX_DIRECT + name).routeId(name).log("from: " + name + " to: " + array[0] + " running..." + System.currentTimeMillis())
                    .multicast()
                    .to(array);
            camelContext.addRouteDefinition(routeDefinition);
        }
    }
    // 构建
    public void buildRoute(RouteDetail routeDetail) throws Exception {
        //获取当前节点的接收者列表
        LinkedList<RouteDetail> routeDetailReceiverListTemp = RouteDetailUtils.getMulticastLists(RouteDetailUtils.getReceiverListBySenderId(routeDetail.getId(), routeDetails));
        Route route = camelContext.getRoute(routeDetail.getEndpointName());
        // 判定上下文中是否存在route,不存在则创建route
        if (null == route) {
            RouteDefinition routeDefinition = new RouteDefinition();
            routeDefinition.from(RouteDetail.PREFIX_DIRECT + routeDetail.getEndpointName())
                    .routeId(routeDetail.getEndpointName())
                    .to(routeDetail.getEndpointUri())
                    .log("from: " + routeDetail.getPreviousId() + " to: " + routeDetail.getEndpointName() + " running..." + System.currentTimeMillis())
                    .process(new CustomDynamicRouterProcessor(routeDetailReceiverListTemp))
                    .dynamicRouter().method(Test.class, "choiceRoute");
            camelContext.addRouteDefinition(routeDefinition);
        }
    }

    // 选择路由执行
    public String choiceRoute(@Header(Exchange.SLIP_ENDPOINT) LinkedList<RouteDetail> list) throws Exception {
        if(!EmptyUtil.isEmptyList(list)) {
            for(RouteDetail routeDetail : list) {
                if(routeDetail.equals(list.getFirst())){
                    return RouteDetail.PREFIX_DIRECT + routeDetail.getMulticast();
                }
            }
        }
        return null;
    }
}
