package com.dah.camel.route.dynimic_formal;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.springframework.util.StringUtils;

import java.util.LinkedList;

public class CustomDynamicRouter {

    public String route(@Header(Exchange.SLIP_ENDPOINT) LinkedList<RouteDetail> routeDetails) {
        if (!EmptyUtil.isEmptyList(routeDetails)) {
            for(RouteDetail routeDetail : routeDetails){
                routeDetail.setHasRun(true);
                RouteDetailUtils.replaceByRouteDetail(routeDetail, DynamicRouteBuilder.routeDetails);
                if (StringUtils.isEmpty(routeDetail.getExpression()) || SpELUtil.parser(routeDetail.getExpression())) {
                    if(routeDetail.equals(routeDetails.getLast())){
                        routeDetails.remove(routeDetail);
                        routeDetails = RouteDetailUtils.getSameLevelListBySenderId(routeDetail.getPreviousId(), DynamicRouteBuilder.routeDetails);
                    } else {
                        routeDetails.remove(routeDetail);
                    }
                    return RouteDetail.PREFIX_DIRECT + routeDetail.getEndpointName();
                }
                if(routeDetail.equals(routeDetails.getLast())){
                    routeDetails.remove(routeDetail);
                    routeDetails = RouteDetailUtils.getSameLevelListBySenderId(routeDetail.getPreviousId(), DynamicRouteBuilder.routeDetails);
                }
                continue;
            }
        }
        return null;
    }

}
