package com.dah.camel.route.dynimic_formal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteEndpoint {

    public static final String PREFIX_ENTPOINT = "endpoint_";

    public static final String PREFIX_DIRECT = "direct:";

    public static final String PREFIX_MULTICAST = "multicast_";

    private String id;

    /**
     * 所属 路由定义 记录ID
     */
    private String routeId;


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
    //当前端点的 Uri
    private String endpointUri;

}
