package com.dah.camel.route.dynimic_formal;

import org.springframework.util.StringUtils;

import java.util.LinkedList;

public class RouteEndpointUtils {

    public static RouteEndpoint getRouteEndpointFrom(LinkedList<RouteEndpoint> routeEndpoints){
        if(!EmptyUtil.isEmptyList(routeEndpoints)){
            for(RouteEndpoint routeEndpoint : routeEndpoints){
                if (StringUtils.isEmpty(routeEndpoint.getPreviousId())) {
                    return routeEndpoint;
                }
            }
        }
        return null;
    }

    public static LinkedList<RouteEndpoint> getReceiverListBySenderId(String routeEndpointId, LinkedList<RouteEndpoint> routeEndpoints){
        if(!EmptyUtil.isEmptyList(routeEndpoints)) {
            LinkedList<RouteEndpoint> routeEndpointLinkedList = new LinkedList<>();
            for (RouteEndpoint routeDetail : routeEndpoints) {
                if (routeDetail.getPreviousId().equals(routeEndpointId)) {
                    routeEndpointLinkedList.add(routeDetail);
                }
            }
            return routeEndpointLinkedList;
        }
        return null;
    }

    public static void replaceByRouteEndpoint(RouteEndpoint routeEndpoint, LinkedList<RouteEndpoint> routeEndpoints){
        if(!EmptyUtil.isEmptyList(routeEndpoints)) {
            for (RouteEndpoint routeEndpointTemp : routeEndpoints) {
                if (routeEndpoint.getId().equals(routeEndpointTemp.getId())) {
                    routeEndpoints.remove(routeEndpointTemp);
                    routeEndpoints.add(routeEndpoint);
                    break;
                }
            }
        }
    }

    public static LinkedList<RouteEndpoint> getMulticastLists(LinkedList<RouteEndpoint> routeEndpoints){
        LinkedList<RouteEndpoint> multicastList = new LinkedList<>();
        if (!EmptyUtil.isEmptyList(routeEndpoints)) {
            for (RouteEndpoint routeEndpoint : routeEndpoints) {
                if(StringUtils.isEmpty(routeEndpoint.getExpression()) || SpELUtil.parser(routeEndpoint.getExpression())) {
                    multicastList.add(routeEndpoint);
                }
            }
        }
       return multicastList;
    }

}
