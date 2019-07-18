package com.dah.camel.route.dynimic_formal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDetail {

    public static final String PREFIX_ENTPOINT = "endpoint_";

    public static final String PREFIX_DIRECT = "direct:";

    private String id;

    /**
     * 所属 路由定义 记录ID
     */
    private String routeDefinitionId;


    /**
     * endpointType 指向某张表
     */
    private String endpointType;

    /**
     * endpointId 指向endpoint_{endpointType}表中的某条数据
     */
    private String endpointId;

    /**
     * 表达式（动态路由的条件）
     */
    private String expression;

    /**
     * 当前控制端点的上一个断点的id（为空则表示为路由的from）
     */
    private String previousId;


    //transient
    //所属 路由ID
    private String routeDefinitionRouteId;
    //当前端点的 Uri
    private String endpointUri;
    //当前端点的名称
    private String endpointName;

    //测试字段
    private boolean hasChild;

    //是否已运行
    private boolean hasRun;

    // 所属广播
    private String multicast;


}
