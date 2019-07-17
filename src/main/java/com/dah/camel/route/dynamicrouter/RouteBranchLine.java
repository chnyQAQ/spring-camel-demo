package com.dah.camel.route.dynamicrouter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RouteBranchLine {

    private String id;

    /**
     * 所属 路由定义 记录ID
     */
    private String routeDefineId;


    /**
     * 当前分支的 from 的ID
     */
    private String routeFromId;

    /**
     * 当前分支的指向to（虚拟）
     */
    private String routeDirectTo;

    /**
     * 当前分支的数据来源（header，body,property)
     */
    private String dataSource;

    /**
     * 当前分支的数据来源字段：如header中的username
     */
    private String key;

    /**
     * 当前分支的数据来源字段类型
     */
    private String dataType;

    /**
     * 当前分支的数据判断操作类型： 具体参照OperationUtils
     */
    private String condition;

    /**
     *   对比值
     */
    private Object compareValue;

    /**
     * 当前分支的指向to（真正）的类型（到哪张表查找）
     */
    private String routeRealToType;

    /**
     * 当前分支的指向to（真正）Id
     */
    private String routeRealToId;

    //translate

    //所属 路由ID
    private String routeId;
    //当前分支的 from 的 Uri
    private String routeFrom;
    //当前分支的指向to（真正）Uri
    private String routeRealTo;
    //实际值（根据dataSource及key取得）
    private Object value;
    //测试字段
    private boolean hasChild;

    public RouteBranchLine(String routeId, String routeFrom, String routeDirectTo, String dataSource, String key, String dataType, String condition, String compareValue, String routeRealTo, String value, boolean hasChild) {
        this.routeId = routeId;
        this.routeFrom = routeFrom;
        this.routeDirectTo = routeDirectTo;
        this.dataSource = dataSource;
        this.key = key;
        this.dataType = dataType;
        this.condition = condition;
        this.compareValue = compareValue;
        this.routeRealTo = routeRealTo;
        this.value = value;
        this.hasChild = hasChild;
    }
}
