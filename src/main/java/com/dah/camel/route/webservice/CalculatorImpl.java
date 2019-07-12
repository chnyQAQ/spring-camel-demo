package com.dah.camel.route.webservice;

import java.util.logging.Logger;

/**
 * 用于路由
 */
public class CalculatorImpl implements Calculator {
    private static final Logger LOG = Logger.getLogger(CalculatorImpl.class.getName());

    @Override
    public String getVersion(String request) {
        LOG.info("Executing operation getVersion");
        System.out.println(request);
        return request;
    }

    @Override
    public Long add(Integer a, Integer b) {
        LOG.info("Executing operation add");
        System.out.println(a);
        System.out.println(b);
        return Long.valueOf((a + b));
    }

    @Override
    public Long subtract(Integer a, Integer b) {
        LOG.info("Executing operation subtract");
        System.out.println(a);
        System.out.println(b);

        return Long.valueOf((a - b));
    }

    @Override
    public Long multiply(Integer a, Integer b) {
        LOG.info("Executing operation multiply");
        System.out.println(a);
        System.out.println(b);
        return Long.valueOf((a/b));
    }
}
