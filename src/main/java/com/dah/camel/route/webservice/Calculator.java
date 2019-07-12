package com.dah.camel.route.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService(targetNamespace = "http://webservice.route.camel.dah.com/", name = "Calculator")
public interface Calculator {

    @WebMethod
    @RequestWrapper(localName = "subtract", targetNamespace = "http://webservice.route.camel.dah.com/", className = "com.dah.camel.route.webservice.Subtract")
    @ResponseWrapper(localName = "subtractResponse", targetNamespace = "http://webservice.route.camel.dah.com/", className = "com.dah.camel.route.webservice.SubtractResponse")
    @WebResult(name = "return", targetNamespace = "")
    public java.lang.Long subtract(
            @WebParam(name = "arg0", targetNamespace = "")
                    java.lang.Integer arg0,
            @WebParam(name = "arg1", targetNamespace = "")
                    java.lang.Integer arg1
    );

    @WebMethod
    @RequestWrapper(localName = "multiply", targetNamespace = "http://webservice.route.camel.dah.com/", className = "com.dah.camel.route.webservice.Multiply")
    @ResponseWrapper(localName = "multiplyResponse", targetNamespace = "http://webservice.route.camel.dah.com/", className = "com.dah.camel.route.webservice.MultiplyResponse")
    @WebResult(name = "return", targetNamespace = "")
    public java.lang.Long multiply(
            @WebParam(name = "arg0", targetNamespace = "")
                    java.lang.Integer arg0,
            @WebParam(name = "arg1", targetNamespace = "")
                    java.lang.Integer arg1
    );

    @WebMethod
    @RequestWrapper(localName = "add", targetNamespace = "http://webservice.route.camel.dah.com/", className = "com.dah.camel.route.webservice.Add")
    @ResponseWrapper(localName = "addResponse", targetNamespace = "http://webservice.route.camel.dah.com/", className = "com.dah.camel.route.webservice.AddResponse")
    @WebResult(name = "return", targetNamespace = "")
    public java.lang.Long add(
            @WebParam(name = "arg0", targetNamespace = "")
                    java.lang.Integer arg0,
            @WebParam(name = "arg1", targetNamespace = "")
                    java.lang.Integer arg1
    );

    @WebMethod
    @RequestWrapper(localName = "getVersion", targetNamespace = "http://webservice.route.camel.dah.com/", className = "com.dah.camel.route.webservice.GetVersion")
    @ResponseWrapper(localName = "getVersionResponse", targetNamespace = "http://webservice.route.camel.dah.com/", className = "com.dah.camel.route.webservice.GetVersionResponse")
    @WebResult(name = "return", targetNamespace = "")
    public java.lang.String getVersion(
            @WebParam(name = "arg0", targetNamespace = "")
                    java.lang.String arg0
    );
}
