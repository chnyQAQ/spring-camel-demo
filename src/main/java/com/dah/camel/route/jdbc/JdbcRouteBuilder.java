package com.dah.camel.route.jdbc;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

//@Component
public class JdbcRouteBuilder extends SpringRouteBuilder {

    @Autowired
    CamelContext camelContext;

    @Autowired
    DataSource dataSource;

    private void initJdbcProperties() {
        SimpleRegistry simpleregistry = new SimpleRegistry();
        simpleregistry.put("DataSource", dataSource);
        ((DefaultCamelContext) camelContext).setRegistry(simpleregistry);
    }

    @Override
    public void configure() throws Exception {
        initJdbcProperties();
        // Apache Camel JDBC组件没法使用 from端点，需要一个触发机制。比如：定时器、或者http调用触发。
        from("timer://queryAward?period=60s").setBody(constant("select * from fire_team")).routeId("jdbcRouteBuilder").autoStartup(false).to("jdbc:DataSource?outputType=SelectList").process(new JdbcProcessor());
    }
}
