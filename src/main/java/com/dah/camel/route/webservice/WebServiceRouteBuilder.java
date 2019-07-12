package com.dah.camel.route.webservice;

import org.apache.camel.component.cxf.CxfComponent;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.stereotype.Component;

import javax.xml.ws.Endpoint;

//@Component
public class WebServiceRouteBuilder extends SpringRouteBuilder {

    private static final String ROUTER_ADDRESS = "http://127.0.0.1:9901/CamelCXFService/calculatorService";

    private static final String SERVICE_CLASS = "serviceClass=com.dah.camel.route.webservice.Calculator";

    private static final String WSDL_LOCATION = "wsdlURL=hello_world.wsdl";

    private static final String ROUTER_ENDPOINT_URI = "cxf://" + ROUTER_ADDRESS + "?dataFormat=POJO&" + SERVICE_CLASS + "&" + WSDL_LOCATION ;

    private static final String SERVICE_ADDRESS = "http://127.0.0.1:9902/ws/calc";


    @Override
    public void configure() throws Exception {

        //发布web service服务
        Endpoint.publish("http://127.0.0.1:9902/ws/calc", new CalculatorImplPortImpl());

        /*CxfComponent cxfComponent = new CxfComponent(getContext());
        CxfEndpoint serviceEndpoint = new CxfEndpoint(SERVICE_ADDRESS, cxfComponent);
        serviceEndpoint.setWsdlURL("hello_world.wsdl");
        serviceEndpoint.setServiceClass(Calculator.class);*/

        // Here we just pass the exception back, don't need to use errorHandler
        errorHandler(noErrorHandler());
        from(ROUTER_ENDPOINT_URI).to("log:WebServiceRouteBuilder?showExchangeId=true").log("${body}");//.to(serviceEndpoint)
    }

}
