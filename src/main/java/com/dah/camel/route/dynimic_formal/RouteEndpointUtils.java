package com.dah.camel.route.dynimic_formal;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RouteEndpointUtils {

    public static RouteEndpoint getRouteEndpointFrom(List<RouteEndpoint> routeEndpoints){
        if(!EmptyUtil.isEmptyList(routeEndpoints)){
            for(RouteEndpoint routeEndpoint : routeEndpoints){
                if (StringUtils.isEmpty(routeEndpoint.getPreviousId())) {
                    return routeEndpoint;
                }
            }
        }
        return null;
    }

    public static List<RouteEndpoint> getReceiverListBySenderId(String routeEndpointId, List<RouteEndpoint> routeEndpoints){
        if(!EmptyUtil.isEmptyList(routeEndpoints)) {
            List<RouteEndpoint> routeEndpointList = new ArrayList<>();
            for (RouteEndpoint routeDetail : routeEndpoints) {
                if (routeDetail.getPreviousId().equals(routeEndpointId)) {
                    routeEndpointList.add(routeDetail);
                }
            }
            return routeEndpointList;
        }
        return null;
    }

    public static List<RouteEndpoint> getMulticastLists(List<RouteEndpoint> routeEndpoints){
        List<RouteEndpoint> multicastList = new ArrayList<>();
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
