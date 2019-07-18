package com.dah.camel.route.dynimic_formal;

import org.apache.camel.*;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

//@Component
public class DynamicRouteBuilder extends SpringRouteBuilder {

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
                    LinkedList<RouteDetail> routeDetailReceiverListTemp = RouteDetailUtils.getReceiverListBySenderId(routeDetailFrom.getId(), routeDetails);
                    RouteDetailUtils.setAllNotRunRouteDetail(routeDetails);
                    routeDetailFrom.setHasRun(true);
                    if (!EmptyUtil.isEmptyList(routeDetailReceiverListTemp)) {
                        //initParam(exchange, routeDetailReceiverListTemp);
                        exchange.getIn().setHeader(Exchange.SLIP_ENDPOINT, routeDetailReceiverListTemp);
                        initRoutes(exchange, routeDetailReceiverListTemp);
                    }
                }
            });
        routeDefinition.dynamicRouter().method(CustomDynamicRouter.class, "route");
        RouteDetailUtils.replaceByRouteDetail(routeDetailFrom, routeDetails);
        camelContext.addRouteDefinition(routeDefinition);
    }

    /**
     * 测试初始化参数
     * @param exchange
     * @param routeDetails
     */
    private void initParam(Exchange exchange, LinkedList<RouteDetail> routeDetails) {
        for (RouteDetail routeDetail : routeDetails) {
            String expression = routeDetail.getExpression();
            if(ExchangeParamParse.hasHeaderParamField(expression)){
                String[] paramFields =  ExchangeParamParse.getHeaderParamField(expression);
                for(String paramField : paramFields){
                    Object exchangeValue = exchange.getIn().getHeader(paramField);
                    expression = ExchangeParamParse.replaceParamToValue(expression, paramField, exchangeValue);
                }
            }

            if(ExchangeParamParse.hasBodyParamField(expression)){
                String[] paramFields =  ExchangeParamParse.getBodyParamField(expression);
            }

            if(ExchangeParamParse.hasPropertyParamField(expression)){
                String[] paramFields =  ExchangeParamParse.getPropertyParamField(expression);
                for(String paramField : paramFields){
                    Object exchangeValue = exchange.getProperties().get(paramField);
                    expression = ExchangeParamParse.replaceParamToValue(expression, paramField, exchangeValue);
                }
            }
            routeDetail.setExpression(expression);
        }
    }



    /**
     * 初始化route
     * @param exchange
     * @param routeDetailReceiverList
     * @throws Exception
     */
    private void initRoutes(Exchange exchange, LinkedList<RouteDetail> routeDetailReceiverList) throws Exception {
        for (RouteDetail routeDetail : routeDetailReceiverList) {
            // 判定上下文中是否存在route,不存在则创建route
            if(null == camelContext.getRoute(routeDetail.getEndpointName())){
                RouteDefinition routeDefinition = new RouteDefinition();
                routeDefinition.from(RouteDetail.PREFIX_DIRECT + routeDetail.getEndpointName())
                    .routeId(routeDetail.getEndpointName())
                    .to(routeDetail.getEndpointUri())
                    .process(new Processor() {
                        @Override
                        public void process(Exchange exchange) throws Exception {
                            LinkedList<RouteDetail> routeDetailReceiverListTemp = RouteDetailUtils.getReceiverListBySenderId(routeDetail.getId(), routeDetails);
                            if(!EmptyUtil.isEmptyList(routeDetailReceiverListTemp)) {
                                //initParam(exchange, routeDetailReceiverListTemp);
                                exchange.getIn().setHeader(Exchange.SLIP_ENDPOINT, routeDetailReceiverListTemp);
                                initRoutes(exchange, routeDetailReceiverListTemp);
                            }
                        }
                    })
                    .log("from: " + routeDetail.getPreviousId() + " to: " + routeDetail.getEndpointName() + " running...")
                    .dynamicRouter().method(CustomDynamicRouter.class, "route");

                camelContext.addRouteDefinition(routeDefinition);
            }
        }
    }

}
