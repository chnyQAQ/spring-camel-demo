package com.dah.camel.infrastructure;

import org.springframework.expression.spel.standard.SpelExpressionParser;

public class CamelParamParser extends SpelExpressionParser {

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
