package com.dah.camel.route.dynamicrouter;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Processor;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

//@Component
public class DynamicRouteBuilder extends SpringRouteBuilder {

    @Autowired
    private CamelContext camelContext;

    @Override
    public void configure() throws Exception {
        //添加测试数据
        List<RouteBranchLine> list = new ArrayList<>();
        list.add(new RouteBranchLine("routerId","routerFromId", "direct:directRouteA", "header", "username", "String", "equal","admin", "realRouteA","", false));
        list.add(new RouteBranchLine("routerId","routerFromId", "direct:directRouteB", "body", "username", "String", "equal","root", "realRouteB","", false));
        list.add(new RouteBranchLine("routerId","routerFromId", "direct:directRouteC", "properties", "username", "String", "equal","user", "direct:directRouteD","", true));
        // 循环动态路由 Dynamic Router
       from("jetty:http://127.0.0.1:8282/dynamicRouterCamel")
               .setHeader("username", constant("admin"))//设置参数 ：header
               .setProperty("username", constant("user"))//设置参数 ： property
               .process(new Processor() {//processor处理参数
                   @Override
                   public void process(Exchange exchange) throws Exception {
                       initRoutes(exchange, list);
                       initParam(exchange, list);
                       exchange.getProperties().put("listRoutes",list);
                      /* exchange.getProperties().put("isMulticast",true);
                       exchange.getProperties().put("",exchange.getProperties().get("aa"));
                       exchange.getProperties().put("",exchange.getIn().getHeader("username"));
                       exchange.getProperties().put("",exchange.getIn().getBody(String.class));*/
                   }
               })
                // 使用dynamicRouter，进行“动态路由”循环，
                // 直到指定的下一个元素为null为止
                .dynamicRouter().method(this, "dynamicDirect");

    }

    /**
     * 测试初始化参数
     * @param exchange
     * @param list
     */
    private void initParam(Exchange exchange, List<RouteBranchLine> list) {
        for (RouteBranchLine routerBranchLine : list) {
            if("header".equals(routerBranchLine.getDataSource())){
                String value = (String) exchange.getIn().getHeader(routerBranchLine.getKey());
                routerBranchLine.setValue(value);
            } else if ("body".equals(routerBranchLine.getDataSource())) {
                //String value = (String) exchange.getIn().getBody();
                //routerBranchLine.setValue(value);
            } else if ("properties".equals(routerBranchLine.getDataSource())){
                String value = (String) exchange.getProperties().get(routerBranchLine.getKey());
                routerBranchLine.setValue(value);
            }
        }
    }

    /**
     * 初始化route
     * @param exchange
     * @param list
     * @throws Exception
     */
    private void initRoutes(Exchange exchange, List<RouteBranchLine> list) throws Exception {
        if (null != list && list.size() > 0){//用于判断是否有child
            for (RouteBranchLine routeBranchLine : list) {
                // 判定上下文中是否存在route,不存在则创建route
                if(null == camelContext.getRoute(routeBranchLine.getRouteDirectTo())){
                    RouteDefinition routeDefinition = new RouteDefinition();
                    if(routeBranchLine.getRouteDirectTo().equals("direct:directRouteC")){//用于测试，运行child
                        routeDefinition.from(routeBranchLine.getRouteDirectTo())
                                .routeId(routeBranchLine.getRouteDirectTo())
                                //.to(routeBranchLine.getRouteRealTo())
                                .log("from: " + routeBranchLine.getRouteDirectTo() + " to: " + routeBranchLine.getRouteRealTo() + " running...");
                    } else {
                        routeDefinition.from(routeBranchLine.getRouteDirectTo())
                                .routeId(routeBranchLine.getRouteDirectTo())
                                .log("from: " + routeBranchLine.getRouteDirectTo() + " to: " + routeBranchLine.getRouteRealTo() + " running...");
                    }
                    /*Processor processor = new Processor() {
                        @Override
                        public void process(Exchange exchange) throws Exception {

                        }
                    };
                    routeDefinition.process(processor);*/
                    camelContext.addRouteDefinition(routeDefinition);
                    System.out.println("创建createdRouteBuilder");
                }
                if (routeBranchLine.isHasChild()){//hasChild只是用于测试，真正情况是从数据库读取
                    List<RouteBranchLine> listChild = new ArrayList<>();
                    listChild.add(new RouteBranchLine("routerId","routerFromId", "direct:directRouteD", "header", "username", "String", "=","admin", "realRouteD","",false));
                    initRoutes(exchange, listChild);
                    initParam(exchange, listChild);
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
        List<RouteBranchLine> routeBranchLines = (List<RouteBranchLine>) properties.get("listRoutes");
        if (routeBranchLines != null && routeBranchLines.size() > 0) {
            for(int i=0,size = routeBranchLines.size() ; i < size; i++){
                RouteBranchLine routerBranchLine = routeBranchLines.get(i);
                if (OperationUtils.EQUAL.equals(routerBranchLine.getCondition())) {
                    if (routerBranchLine.getValue() == routerBranchLine.getCompareValue()) {
                        routeBranchLines = routeBranchLines.subList(i + 1, size);
                        properties.put("listRoutes",routeBranchLines);
                        return routerBranchLine.getRouteDirectTo();
                    }
                }
                continue;
            }
        }
        return null;
    }

}
