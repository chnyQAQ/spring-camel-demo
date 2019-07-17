package com.dah.camel.route.dynimic_formal;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Processor;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Stream;

@Component
public class DynamicRouteBuilder extends SpringRouteBuilder {

    @Autowired
    private CamelContext camelContext;

    public static LinkedList<RouteDetail> routeDetails;

    @Override
    public void configure() throws Exception {
        //添加测试数据
        routeDetails = new LinkedList<>();
        routeDetails.add(new RouteDetail("routeDetail_from","route_from_id", "rest", "endpoint_from_id", "", "", "route_from","jetty:http://127.0.0.1:8282/dynamicRouterCamel", "endpoint_from", true));
        routeDetails.add(new RouteDetail("routeDetail_1_id","route_1_id", "rest", "endpoint_1_id", "-5 < 0", "routeDetail_from", "route_from","direct:endpoint_4", "endpoint_1", true));
        routeDetails.add(new RouteDetail("routeDetail_2_id","route_1_id", "rest", "endpoint_2_id", "-5 < 5", "routeDetail_from", "route_from","mock:result_endpoint_2_uri", "endpoint_2", false));
        routeDetails.add(new RouteDetail("routeDetail_3_id","route_1_id", "rest", "endpoint_3_id", "-5 > 5", "routeDetail_from", "route_from","mock:result_endpoint_3_uri", "endpoint_3", false));
        routeDetails.add(new RouteDetail("routeDetail_4_id","route_1_id", "rest", "endpoint_4_id", "", "routeDetail_1_id", "route_from","mock:result_endpoint_4_uri", "endpoint_4", false));
        routeDetails.add(new RouteDetail("routeDetail_5_id","route_1_id", "rest", "endpoint_5_id", "", "routeDetail_1_id", "route_from","mock:result_endpoint_5_uri", "endpoint_5", false));

        RouteDetail routeDetailFrom = getRouteDetailFrom(routeDetails);


        // 循环动态路由 Dynamic Router
        from(routeDetailFrom.getEndpointUri()).routeId(routeDetailFrom.getEndpointName())
               .setHeader("count", constant(-5))//设置参数 ：header
               .setProperty("age", constant(15))//设置参数 ： property
               .process(new Processor() {//processor处理参数
                   @Override
                   public void process(Exchange exchange) throws Exception {
                       exchange.getProperties().put("currentRouteReceiverList", routeDetails);
                       LinkedList<RouteDetail> routeDetailReceiverListTemp = getReceiverListBySenderId(routeDetailFrom.getId(), routeDetails);
                       //initParam(exchange, routeDetailReceiverListTemp);
                       initRoutes(exchange, routeDetailReceiverListTemp);
                   }
               })
                // 使用dynamicRouter，进行“动态路由”循环，
                // 直到指定的下一个元素为null为止
                .dynamicRouter().method(this, "dynamicDirect");

    }

    /**
     * 测试初始化参数
     * @param exchange
     * @param routeDetails
     */
    private void initParam(Exchange exchange, List<RouteDetail> routeDetails) {
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

    public RouteDetail getRouteDetailFrom(List<RouteDetail> routeDetails){
        for(RouteDetail routeDetail : routeDetails){
            if (StringUtils.isEmpty(routeDetail.getPreviousId())) {
                return routeDetail;
            }
        }
        return null;
    }

    public LinkedList<RouteDetail> getReceiverListBySenderId(String senderId, List<RouteDetail> routeDetails){
        LinkedList<RouteDetail> routeDetailReceiverListsTemp = new LinkedList<>();
        for(RouteDetail routeDetail : routeDetails){
            if (routeDetail.getPreviousId().equals(senderId)) {
                routeDetailReceiverListsTemp.add(routeDetail);
            }
        }
        return routeDetailReceiverListsTemp;
    }

    /**
     * 初始化route
     * @param exchange
     * @param routeDetailReceiverList
     * @throws Exception
     */
    private void initRoutes(Exchange exchange, List<RouteDetail> routeDetailReceiverList) throws Exception {
        if (null != routeDetailReceiverList && routeDetailReceiverList.size() > 0){//用于判断是否有receiver
            for (RouteDetail routeDetail : routeDetailReceiverList) {
                // 判定上下文中是否存在route,不存在则创建route
                if(null == camelContext.getRoute(RouteDetail.PREFIX_DIRECT + routeDetail.getEndpointName())){
                    RouteDefinition routeDefinition = new RouteDefinition();
                    routeDefinition.from(RouteDetail.PREFIX_DIRECT + routeDetail.getEndpointName())
                            .routeId(RouteDetail.PREFIX_DIRECT + routeDetail.getEndpointName())
                            .to(routeDetail.getEndpointUri())
                            .log("from: " + routeDetail.getPreviousId() + " to: " + routeDetail.getEndpointName() + " running...");
                    camelContext.addRouteDefinition(routeDefinition);
                }
                LinkedList<RouteDetail> routeDetailReceiverListTemp = getReceiverListBySenderId(routeDetail.getId(), routeDetails);
                //initParam(exchange, routeDetailReceiverListTemp);
                if(routeDetailReceiverListTemp != null && routeDetailReceiverListTemp.size() > 0) {
                    initRoutes(exchange, routeDetailReceiverListTemp);
                }
            }
        }

    }

    /**
     * 动态循环路由
     * @param properties
     * @return
     */
    public String dynamicDirect(@ExchangeProperties Map<String, Object> properties) {
        RouteDetail routeDetailFrom = getRouteDetailFrom(routeDetails);
        LinkedList<RouteDetail> routeDetailReceiverListTemp = getReceiverListBySenderId(routeDetailFrom.getId(), routeDetails);
        int size = routeDetailReceiverListTemp.size();
        if (routeDetailReceiverListTemp != null && size > 0) {
            for(RouteDetail routeDetail : routeDetailReceiverListTemp){
                if (StringUtils.isEmpty(routeDetail.getExpression()) || SpELUtil.parser(routeDetail.getExpression())) {
                    routeDetailReceiverListTemp.remove(routeDetail);
                    return RouteDetail.PREFIX_DIRECT + routeDetail.getEndpointName();
                }
                continue;
            }
        }
        return null;
    }

}
