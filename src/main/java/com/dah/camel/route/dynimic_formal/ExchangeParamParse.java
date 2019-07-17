package com.dah.camel.route.dynimic_formal;

import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

public class ExchangeParamParse {

    public static void main(String[] args) throws NoSuchMethodException {
        StandardEvaluationContext context = new StandardEvaluationContext();
        Method getHeaderParamField = ExchangeParamParse.class.getDeclaredMethod("getHeaderParamField", String.class);
        Method getBodyParamField = ExchangeParamParse.class.getDeclaredMethod("getBodyParamField", String.class);
        Method getPropertyParamField = ExchangeParamParse.class.getDeclaredMethod("getPropertyParamField", String.class);
        Method hasHeaderParamField = ExchangeParamParse.class.getDeclaredMethod("hasHeaderParamField", String.class);
        Method hasBodyParamField = ExchangeParamParse.class.getDeclaredMethod("hasBodyParamField", String.class);
        Method hasPropertyParamField = ExchangeParamParse.class.getDeclaredMethod("hasPropertyParamField", String.class);
        Method replaceParamToValue = ExchangeParamParse.class.getDeclaredMethod("replaceParamToValue", String.class);
        context.registerFunction("getHeaderParamField", getHeaderParamField);
        context.registerFunction("getBodyParamField", getBodyParamField);
        context.registerFunction("getPropertyParamField", getPropertyParamField);
        context.registerFunction("hasHeaderParamField", hasHeaderParamField);
        context.registerFunction("hasBodyParamField", hasBodyParamField);
        context.registerFunction("hasPropertyParamField", hasPropertyParamField);
        context.registerFunction("replaceParamToValue", replaceParamToValue);
    }

    public static boolean hasHeaderParamField(String expression){
        if(expression.contains("header")){
            return true;
        }
        return false;
    }

    public static boolean hasBodyParamField(String expression){
        if(expression.contains("body")){
            return true;
        }
        return false;
    }

    public static boolean hasPropertyParamField(String expression){
        if(expression.contains("property")){
            return true;
        }
        return false;
    }

    public static String[] getHeaderParamField(String expression){
        return null;
    }

    public static String[] getBodyParamField(String expression){
        return null;
    }

    public static String[] getPropertyParamField(String expression){
        return null;
    }

    public static String replaceParamToValue (String expression, String exchangeParam, Object exchangeValue) {
        expression.replace(exchangeParam, (CharSequence) exchangeValue);
        return expression;
    }

}
