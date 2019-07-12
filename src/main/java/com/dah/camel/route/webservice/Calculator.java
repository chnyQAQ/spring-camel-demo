package com.dah.camel.route.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface Calculator {

    @WebMethod
    public Long subtract(Integer arg0, Integer arg1);

    @WebMethod
    public Long multiply(Integer arg0, Integer arg1);

    @WebMethod
    public Long add(Integer arg0, Integer arg1);

    @WebMethod
    public String getVersion(String arg0);
}
