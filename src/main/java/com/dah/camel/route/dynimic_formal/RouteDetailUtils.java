package com.dah.camel.route.dynimic_formal;

import org.springframework.util.StringUtils;

import java.util.LinkedList;

public class RouteDetailUtils {

    public static RouteDetail getRouteDetailFrom(LinkedList<RouteDetail> routeDetails){
        if(!EmptyUtil.isEmptyList(routeDetails)){
            for(RouteDetail routeDetail : routeDetails){
                if (StringUtils.isEmpty(routeDetail.getPreviousId())) {
                    return routeDetail;
                }
            }
        }
        return null;
    }

    public static RouteDetail getRouteDetailById(String id, LinkedList<RouteDetail> routeDetails){
        if(!EmptyUtil.isEmptyList(routeDetails)) {
            for (RouteDetail routeDetail : routeDetails) {
                if (id.equals(routeDetail.getId())) {
                    return routeDetail;
                }
            }
        }
        return null;
    }

    public static LinkedList<RouteDetail> getReceiverListBySenderId(String senderId, LinkedList<RouteDetail> routeDetails){
        if(!EmptyUtil.isEmptyList(routeDetails)) {
            LinkedList<RouteDetail> routeDetailLists = new LinkedList<>();
            for (RouteDetail routeDetail : routeDetails) {
                if (routeDetail.getPreviousId().equals(senderId)) {
                    routeDetailLists.add(routeDetail);
                }
            }
            return routeDetailLists;
        }
        return null;
    }

    public static LinkedList<RouteDetail> getSameLevelListBySenderId(String senderId, LinkedList<RouteDetail> routeDetails){
        if(!EmptyUtil.isEmptyList(routeDetails)) {
            senderId = getRouteDetailById(senderId, routeDetails).getPreviousId();
            LinkedList<RouteDetail> routeDetailLists = new LinkedList<>();
            for (RouteDetail routeDetail : routeDetails) {
                if (routeDetail.getPreviousId().equals(senderId) && !routeDetail.isHasRun()) {
                    routeDetailLists.add(routeDetail);
                }
            }
            return routeDetailLists;
        }
        return null;
    }

    public static void replaceByRouteDetail(RouteDetail routeDetail, LinkedList<RouteDetail> routeDetails){
        if(!EmptyUtil.isEmptyList(routeDetails)) {
            for (RouteDetail routeDetailTemp : routeDetails) {
                if (routeDetail.getId().equals(routeDetailTemp.getId())) {
                    routeDetails.remove(routeDetailTemp);
                    routeDetails.add(routeDetail);
                    break;
                }
            }
        }
    }

    public static void setAllNotRunRouteDetail(LinkedList<RouteDetail> routeDetails){
        if(!EmptyUtil.isEmptyList(routeDetails)) {
            for (RouteDetail routeDetailTemp : routeDetails) {
                routeDetailTemp.setHasRun(false);
                routeDetailTemp.setMulticast(null);
            }
        }
    }
}
