package com.dah.camel.infrastructure;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.io.Serializable;

public class CustomParserContext implements Serializable, ParserContext {

    private static final long serialVersionUID = 1L;

    private final String expressionPrefix;

    private final String expressionSuffix;

    public CustomParserContext() {
        this("#{", "}");
    }

    public CustomParserContext(String expressionPrefix, String expressionSuffix) {
        this.expressionPrefix = expressionPrefix;
        this.expressionSuffix = expressionSuffix;
    }

    @Override
    public boolean isTemplate() {
        return true;
    }

    @Override
    public String getExpressionPrefix() {
        return this.expressionPrefix;
    }

    @Override
    public String getExpressionSuffix() {
        return this.expressionSuffix;
    }

    public static void main(String[] args) {
        ExpressionParser parser = new SpelExpressionParser();
        String randomPhrase = parser.parseExpression(
                "random number is #{T(java.lang.Math).random()}",
                new CustomParserContext
                        ()).getValue(String.class);
        System.out.println(randomPhrase);
    }

}
