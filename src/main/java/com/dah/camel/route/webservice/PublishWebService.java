package com.dah.camel.route.webservice;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

import javax.xml.ws.Endpoint;

public class PublishWebService {

    private static final String SERVICE_ADDRESS = "http://127.0.0.1:9902/ws/calc";

    public static void main(String[] args) {
        //发布web service服务 1
        //Endpoint.publish(SERVICE_ADDRESS, new CalculatorImplPortImpl());
        //发布服务 2
        JaxWsServerFactoryBean factoryBean = new JaxWsServerFactoryBean();
        factoryBean.setAddress(SERVICE_ADDRESS); // 设置暴露地址
        factoryBean.setServiceClass(Calculator.class); // 接口类
        factoryBean.setServiceBean(new CalculatorImplPortImpl()); // 设置实现类
        factoryBean.create();
    }
}
