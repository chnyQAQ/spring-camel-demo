package com.dah.camel.route.mybatis;

import org.apache.camel.component.mybatis.MyBatisComponent;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

//@Component
public class MybatisRouteBuilder extends SpringRouteBuilder {

    @Bean(name="mybatis1")
    public MyBatisComponent myBatisComponent1(SqlSessionFactory sqlSessionFactory) {
        MyBatisComponent result = new MyBatisComponent();
        result.setSqlSessionFactory(sqlSessionFactory);
        return result;
    }

    @Bean(name="mybatis")
    public MyBatisComponent myBatisComponent(SqlSessionFactory sqlSessionFactory) {
        MyBatisComponent result = new MyBatisComponent();
        result.setSqlSessionFactory(sqlSessionFactory);
        return result;
    }

    @Override
    public void configure() throws Exception {
        from("timer:mybatis?repeatCount=1&delay=5000").to("mybatis:com.dah.camel.domain.user.UserMapper.getAll?statementType=SelectList").log("${body}");
        from("timer:mybatis1?period=10s").to("mybatis1:com.dah.camel.domain.role.RoleMapper.getAll?statementType=SelectList").log("${body}").to("mock:result");//mock:result模拟结束
    }



}
