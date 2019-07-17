package com.dah.camel.route.dynimic_formal;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

public class SpELUtil {

    public static ExpressionParser parser = new SpelExpressionParser();

    public static boolean parser(String expression){
        Expression exp = parser.parseExpression(expression);
        boolean value = (boolean) exp.getValue();
        return value;
    }

    public static void main(String[] args) throws NoSuchMethodException {
        //创建ExpressionParser解析表达式
        ExpressionParser parser = new SpelExpressionParser();
        //表达式放置
        String expString = "header = admin";
        String header = "admin";

        expString = expString.replace("header", header.toString());

        System.out.println(expString);
        Expression exp = parser.parseExpression(10+"年"+3+"个月");
        //执行表达式，默认容器是spring本身的容器：ApplicationContext
        Object value = exp.getValue();
        System.out.println(value);

    }
}
