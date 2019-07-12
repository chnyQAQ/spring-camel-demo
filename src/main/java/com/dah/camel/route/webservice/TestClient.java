package com.dah.camel.route.webservice;

public class TestClient {

    public static void main(String[] args) {
        CalculatorImplService camelCXFServiceImplService = new CalculatorImplService();

        Calculator camelCXFServiceInter = camelCXFServiceImplService.getCalculatorImplPort();

        String result = camelCXFServiceInter.getVersion("cyx00000");
        Long result2 = camelCXFServiceInter.add(2, 5);

        System.out.println("result : " + result);
        System.out.println("result2 : " + result2);
    }
}
