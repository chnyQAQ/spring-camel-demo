package com.dah.camel.route.webservice;

import javax.xml.ws.Endpoint;

public class PublishWebService {

    //执行后，访问http://127.0.0.1:9902/ws/calc?wsdl获取xml文件，保存
    //保存文件后关闭
    public static void main(String[] args) {
        Endpoint.publish("http://127.0.0.1:9902/ws/calc", new CalculatorImpl());
    }
}
