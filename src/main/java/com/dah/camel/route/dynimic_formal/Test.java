package com.dah.camel.route.dynimic_formal;

import org.apache.camel.*;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.from(routeDetailFrom.getEndpointUri())
            .routeId(routeDetailFrom.getEndpointName())
            .process(new Processor() {//processor处理参数
                @Override
                public void process(Exchange exchange) throws Exception {
                    RouteDetailUtils.setAllNotRunRouteDetail(routeDetails);
                    LinkedList<RouteDetail> routeDetailReceiverListTemp = RouteDetailUtils.getReceiverListBySenderId(routeDetailFrom.getId(), routeDetails);
                    exchange.getIn().setHeader(Exchange.SLIP_ENDPOINT, getMulticastLists(routeDetailReceiverListTemp));
                    customProcessor(exchange, routeDetailReceiverListTemp);
                }
            })
            .dynamicRouter().method(Test.class, "choiceRoute");
        camelContext.addRouteDefinition(routeDefinition);
    }

    public void customProcessor(Exchange exchange, LinkedList<RouteDetail> routeDetailReceiverListTemp) throws Exception {
        LinkedList<RouteDetail> isMu = new LinkedList<>();
        if (!EmptyUtil.isEmptyList(routeDetailReceiverListTemp)) {
            //initParam(exchange, routeDetailReceiverListTemp);
            for (RouteDetail childRoute : routeDetailReceiverListTemp) {
                if(null != camelContext.getRoute(childRoute.getMulticast())) {
                    camelContext.removeRoute(childRoute.getMulticast());
                }
                if(StringUtils.isEmpty(childRoute.getExpression()) || SpELUtil.parser(childRoute.getExpression())) {
                    isMu.add(childRoute);
                } else {
                    childRoute.setMulticast(null);
                }
            }
            buildMulticast(isMu);
            initRoutes(exchange, routeDetailReceiverListTemp);
        }
    }


    public LinkedList<RouteDetail> getMulticastLists(LinkedList<RouteDetail> routeDetailReceiverListTemp){
        LinkedList<RouteDetail> isMu = new LinkedList<>();
        if (!EmptyUtil.isEmptyList(routeDetailReceiverListTemp)) {
            //initParam(exchange, routeDetailReceiverListTemp);
            for (RouteDetail childRoute : routeDetailReceiverListTemp) {
                if(StringUtils.isEmpty(childRoute.getExpression()) || SpELUtil.parser(childRoute.getExpression())) {
                    isMu.add(childRoute);
                }
            }
        }
        return isMu;
    }


    // 初始化路由
    public void initRoutes (Exchange exchange, List<RouteDetail> routes) throws Exception {
        for (RouteDetail routeDetail : routes) {
            if (null == camelContext.getRoute(routeDetail.getEndpointName())) {
                LinkedList<RouteDetail> routeDetailReceiverListTemp = RouteDetailUtils.getReceiverListBySenderId(routeDetail.getId(), routeDetails);
                customProcessor(exchange, routeDetailReceiverListTemp);
                buildRoute(routeDetail);
            }
        }
    }

    // 构建广播
    public void buildMulticast(List<RouteDetail> routeDetailList) throws Exception {

        String name = "multicast_" + UUID.randomUUID().toString();
        if(routeDetailList.size() > 0){
            int len = routeDetailList.size();
            String [] array = new String[len];
            for (int i=0; i< len; i++){
                array[i] = RouteDetail.PREFIX_DIRECT + routeDetailList.get(i).getEndpointName();
            }

            System.out.println(name);
            RouteDefinition routeDefinition = new RouteDefinition();
            routeDefinition.from(RouteDetail.PREFIX_DIRECT + name).routeId(name)
                    .multicast()
                    .to(array).log("from: " + name + " to: " + array[0] + " running...");
            camelContext.addRouteDefinition(routeDefinition);
            for (RouteDetail routeDetail : routeDetailList) {
                routeDetail.setMulticast(name);
                RouteDetailUtils.replaceByRouteDetail(routeDetail, routeDetails);
            }
        }
    }
    // 构建
    public void buildRoute(RouteDetail route) throws Exception {
        // 判定上下文中是否存在route,不存在则创建route
        if (null == camelContext.getRoute(RouteDetail.PREFIX_DIRECT + route.getEndpointName())) {
            RouteDefinition routeDefinition = new RouteDefinition();
            routeDefinition.from(RouteDetail.PREFIX_DIRECT + route.getEndpointName())
                    .routeId(RouteDetail.PREFIX_DIRECT + route.getEndpointName())
                    .to(route.getEndpointUri())
                    .log("from: " + route.getPreviousId() + " to: " + route.getEndpointName() + " running...")
                    .dynamicRouter().method(Test.class, "choiceRoute");
            camelContext.addRouteDefinition(routeDefinition);
        }
    }

    // 选择路由执行
    public String choiceRoute(@Header(Exchange.SLIP_ENDPOINT) LinkedList<RouteDetail> routeDetailList) {
        if(!EmptyUtil.isEmptyList(routeDetailList)) {
            for(RouteDetail routeDetail : routeDetailList) {
                routeDetail.setHasRun(true);
                RouteDetailUtils.replaceByRouteDetail(routeDetail, routeDetails);
                if (StringUtils.isEmpty(routeDetail.getExpression()) || SpELUtil.parser(routeDetail.getExpression())) {
                    //choiceRoute(properties);
                    if(routeDetail.equals(routeDetailList.get(routeDetailList.size()-1))) {
                        routeDetails = RouteDetailUtils.getSameLevelListBySenderId(routeDetail.getPreviousId(), routeDetails);
                        return RouteDetail.PREFIX_DIRECT + routeDetail.getMulticast();
                    }
                }
                continue;
            }
        }
        return null;
    }

}
